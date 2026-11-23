package com.st_albanito.solarsizer.simulation;

import com.st_albanito.solarsizer.auth.User;
import com.st_albanito.solarsizer.catalog.*;
import com.st_albanito.solarsizer.irradiance.IrradianceTypicalDay;
import com.st_albanito.solarsizer.irradiance.IrradianceTypicalDayRepository;
import com.st_albanito.solarsizer.project.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimulationService {

    private static final int[] DAYS_IN_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    // Normalized diurnal weight profile for MANUAL daily kWh input (sums to 1.00)
    private static final double[] MANUAL_HOURLY_WEIGHTS = {
            0.020, 0.018, 0.016, 0.016, 0.018, 0.025, // 00:00 - 05:00
            0.042, 0.055, 0.050, 0.042, 0.040, 0.042, // 06:00 - 11:00
            0.045, 0.042, 0.040, 0.042, 0.048, 0.060, // 12:00 - 17:00
            0.075, 0.082, 0.078, 0.065, 0.048, 0.031  // 18:00 - 23:00
    };

    private final ProjectRepository projectRepository;
    private final SimulationRepository simulationRepository;
    private final SimulationHourlyRepository simulationHourlyRepository;
    private final IrradianceTypicalDayRepository irradianceRepository;
    private final PanelRepository panelRepository;
    private final BatteryRepository batteryRepository;
    private final InverterRepository inverterRepository;

    @Transactional
    public SimulationResponse runSimulation(User currentUser, UUID projectId, RunSimulationRequest request) {
        Project project = projectRepository.findByIdAndUserId(projectId, currentUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        ProjectResponse projectMetrics = ProjectResponse.from(project);
        double dailyLoadKwh = projectMetrics.calculatedDailyLoadKwh().doubleValue();
        if (dailyLoadKwh <= 0.0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Project has 0 kWh daily load. Add appliances or set manualDailyKwh before simulating."
            );
        }

        // 1. Load 288 hourly irradiance records
        List<IrradianceTypicalDay> irradianceRows =
                irradianceRepository.findByIdLocationIdOrderByIdMonthAscIdHourAsc(project.getLocation().getId());
        if (irradianceRows.size() < 288) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Location lacks a complete 288-hour irradiance profile"
            );
        }

        Map<Short, List<IrradianceTypicalDay>> irradianceByMonth = irradianceRows.stream()
                .collect(Collectors.groupingBy(r -> r.getId().getMonth(), TreeMap::new, Collectors.toList()));

        // Compute monthly PSH, annual average PSH, and worst-month PSH
        double sumPsh = 0.0;
        double worstMonthPsh = Double.MAX_VALUE;
        for (List<IrradianceTypicalDay> monthRows : irradianceByMonth.values()) {
            double monthPsh = monthRows.stream()
                    .mapToDouble(r -> r.getGhiWM2().doubleValue())
                    .sum() / 1000.0;
            sumPsh += monthPsh;
            if (monthPsh < worstMonthPsh) {
                worstMonthPsh = monthPsh;
            }
        }
        double annualAvgPsh = sumPsh / 12.0;

        String mode = project.getSystemMode().toUpperCase();
        double designPsh = "ON_GRID".equals(mode) ? annualAvgPsh : Math.max(worstMonthPsh, 1.5);
        double systemLosses = project.getSystemLossesPct().doubleValue() / 100.0;
        double performanceRatio = Math.max(0.50, 1.0 - systemLosses);

        // 2. Select Panel, Battery, and Inverter
        Panel panel = resolvePanel(project);
        Battery battery = "ON_GRID".equals(mode) ? null : resolveBattery(project);

        double peakRunningW = projectMetrics.peakRunningWatts().doubleValue();
        double peakSurgeW = projectMetrics.peakSurgeWatts().doubleValue();
        if ("MANUAL".equalsIgnoreCase(project.getLoadInputMethod())) {
            // Estimate peak running and surge for manual kWh load
            peakRunningW = (dailyLoadKwh * 1000.0 / 24.0) * 2.2;
            peakSurgeW = peakRunningW * 1.8;
        }

        Inverter inverter = resolveInverter(mode, peakRunningW, peakSurgeW,
                request != null ? request.inverterId() : null);

        // 3. Size PV Array
        double requiredPvKwp = dailyLoadKwh / (designPsh * performanceRatio);
        double panelWatts = panel.getRatedWatts().doubleValue();
        int panelCount = Math.max(1, (int) Math.ceil((requiredPvKwp * 1000.0) / panelWatts));
        double actualPvKwp = (panelCount * panelWatts) / 1000.0;

        // 4. Size Battery Bank
        int batteryCount = 0;
        double actualBatteryKwh = 0.0;
        double usableDod = 0.80;
        double roundTripEff = 0.90;
        double systemVoltage = 48.0;

        if (battery != null) {
            usableDod = battery.getUsableDodPct().doubleValue() / 100.0;
            roundTripEff = battery.getRoundTripEffPct().doubleValue() / 100.0;
            systemVoltage = battery.getNominalVoltage().doubleValue();

            double autonomyDays = switch (mode) {
                case "OFF_GRID" -> project.getAutonomyDays() != null
                        ? project.getAutonomyDays().doubleValue() : 1.5;
                case "HYBRID" -> project.getBackupHoursTarget() != null
                        ? project.getBackupHoursTarget().doubleValue() / 24.0 : 0.35;
                default -> 0.0;
            };

            double requiredUsableKwh = dailyLoadKwh * autonomyDays;
            double unitCapacityKwh = battery.getCapacityKwh().doubleValue();
            batteryCount = Math.max(1, (int) Math.ceil(requiredUsableKwh / (unitCapacityKwh * usableDod * roundTripEff)));
            actualBatteryKwh = batteryCount * unitCapacityKwh;
        } else if (inverter.getBatteryVoltage() != null && inverter.getBatteryVoltage().doubleValue() > 0) {
            systemVoltage = inverter.getBatteryVoltage().doubleValue();
        }

        // MPPT Charge Controller sizing (NEC 1.25 safety factor)
        double controllerAmps = ((actualPvKwp * 1000.0) / systemVoltage) * 1.25;

        // 5. Build 24-Hour Load Profile (W)
        double[] hourlyLoadW = build24HourLoadProfile(project, dailyLoadKwh);

        // 6. Execute 288-Step Hourly Simulation
        double inverterEff = inverter.getEfficiencyPct().doubleValue() / 100.0;
        double tempCoeffPct = panel.getTempCoeffPmax().doubleValue(); // negative, e.g. -0.35 %/C
        double chargeEff = Math.sqrt(roundTripEff);
        double dischargeEff = Math.sqrt(roundTripEff);

        double minSocPct = (1.0 - usableDod) * 100.0;
        double socPct = "ON_GRID".equals(mode) ? 0.0 : 80.0; // Start simulation at 80% SoC

        double annualPvKwh = 0.0;
        double annualLoadKwh = 0.0;
        double annualGridImportKwh = 0.0;
        double annualGridExportKwh = 0.0;
        double annualUnmetKwh = 0.0;
        double annualDirectAndBattSelfConsumedKwh = 0.0;

        String label = (request != null && request.label() != null && !request.label().isBlank())
                ? request.label().trim()
                : mode + " Run - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        Simulation simulation = Simulation.builder()
                .project(project)
                .label(label)
                .status("COMPLETED")
                .dailyLoadKwh(bd(dailyLoadKwh, 2))
                .peakLoadW(bd(peakRunningW, 1))
                .pvKwp(bd(actualPvKwp, 2))
                .panelCount(panelCount)
                .batteryKwh(bd(actualBatteryKwh, 2))
                .batteryCount(batteryCount)
                .inverterContinuousW(inverter.getContinuousW())
                .inverterSurgeW(inverter.getSurgeW())
                .controllerAmps(bd(controllerAmps, 1))
                // Placeholder totals updated immediately after the 288-step loop
                .annualPvKwh(BigDecimal.ZERO)
                .selfConsumptionPct(BigDecimal.ZERO)
                .gridImportKwh(BigDecimal.ZERO)
                .gridExportKwh(BigDecimal.ZERO)
                .unmetLoadKwh(BigDecimal.ZERO)
                .capex(BigDecimal.ZERO)
                .annualSavings(BigDecimal.ZERO)
                .assumptionsJson(buildAssumptionsMap(panel, battery, inverter, designPsh, annualAvgPsh, worstMonthPsh, performanceRatio))
                .build();

        Simulation savedSimulation = simulationRepository.save(simulation);
        List<SimulationHourly> hourlyEntities = new ArrayList<>(288);

        for (short m = 1; m <= 12; m++) {
            int daysInMonth = DAYS_IN_MONTH[m - 1];
            List<IrradianceTypicalDay> monthIrradiance = irradianceByMonth.get(m);

            // Determine outage probability & window for HYBRID mode
            GridOutageProfile outage = project.getOutageProfile();
            double outageFraction = 0.0;
            int outageStart = -1;
            int outageEnd = -1;
            if ("HYBRID".equals(mode) && outage != null && outage.getOutagesPerMonth() > 0) {
                outageFraction = Math.min(1.0, (double) outage.getOutagesPerMonth() / daysInMonth);
                outageStart = outage.getTypicalStartHour();
                outageEnd = outageStart + (int) Math.ceil(outage.getAvgDurationHours().doubleValue());
            }

            for (IrradianceTypicalDay irr : monthIrradiance) {
                short h = irr.getId().getHour();
                double ghi = irr.getGhiWM2().doubleValue();
                double tempC = irr.getTempC().doubleValue();
                double loadW = hourlyLoadW[h];

                // Cell temperature & thermal derating (NOCT = 45 C)
                double cellTemp = tempC + ((45.0 - 20.0) / 800.0) * ghi;
                double tempDerate = Math.max(0.70, Math.min(1.10, 1.0 + (tempCoeffPct / 100.0) * (cellTemp - 25.0)));

                double pvW = actualPvKwp * 1000.0 * (ghi / 1000.0) * performanceRatio * tempDerate * inverterEff;
                double gridImportW = 0.0;
                double gridExportW = 0.0;
                double unmetW = 0.0;

                boolean gridAvailable = switch (mode) {
                    case "ON_GRID" -> true;
                    case "OFF_GRID" -> false;
                    case "HYBRID" -> {
                        if (outageStart < 0) yield true;
                        int wrappedHour = (h < outageStart) ? h + 24 : h;
                        boolean inOutageWindow = wrappedHour >= outageStart && wrappedHour < outageEnd;
                        yield !inOutageWindow || (outageFraction < 0.5);
                    }
                    default -> true;
                };

                double netW = pvW - loadW;
                if (netW >= 0) {
                    // Surplus solar power
                    if (actualBatteryKwh > 0 && socPct < 100.0) {
                        double roomKwh = ((100.0 - socPct) / 100.0) * actualBatteryKwh;
                        double maxChargeInputW = (roomKwh * 1000.0) / chargeEff;
                        double absorbedW = Math.min(netW, maxChargeInputW);
                        socPct += ((absorbedW * chargeEff) / 1000.0 / actualBatteryKwh) * 100.0;
                        netW -= absorbedW;
                    }
                    if (netW > 0 && gridAvailable) {
                        gridExportW = netW;
                    }
                } else {
                    // Load exceeds PV generation
                    double deficitW = -netW;
                    if (actualBatteryKwh > 0 && socPct > minSocPct) {
                        double availableKwh = ((socPct - minSocPct) / 100.0) * actualBatteryKwh;
                        double maxDischargeOutputW = availableKwh * 1000.0 * dischargeEff;
                        double suppliedByBattW = Math.min(deficitW, maxDischargeOutputW);
                        socPct -= ((suppliedByBattW / dischargeEff) / 1000.0 / actualBatteryKwh) * 100.0;
                        deficitW -= suppliedByBattW;
                    }
                    if (deficitW > 0) {
                        if (gridAvailable) {
                            gridImportW = deficitW;
                        } else {
                            unmetW = deficitW;
                        }
                    }
                }

                // Accumulate annual energy (Wh -> kWh * daysInMonth)
                double monthFactor = daysInMonth / 1000.0;
                annualPvKwh += pvW * monthFactor;
                annualLoadKwh += loadW * monthFactor;
                annualGridImportKwh += gridImportW * monthFactor;
                annualGridExportKwh += gridExportW * monthFactor;
                annualUnmetKwh += unmetW * monthFactor;
                annualDirectAndBattSelfConsumedKwh += Math.max(0.0, loadW - gridImportW - unmetW) * monthFactor;

                hourlyEntities.add(SimulationHourly.builder()
                        .id(new SimulationHourlyId(savedSimulation.getId(), m, h))
                        .simulation(savedSimulation)
                        .loadW(bd(loadW, 1))
                        .pvW(bd(pvW, 1))
                        .batterySocPct(bd(socPct, 2))
                        .gridImportW(bd(gridImportW, 1))
                        .gridExportW(bd(gridExportW, 1))
                        .unmetW(bd(unmetW, 1))
                        .build());
            }
        }

        simulationHourlyRepository.saveAll(hourlyEntities);

        // 7. Financial & Self-Consumption Metrics
        double selfConsumptionPct = annualPvKwh > 0
                ? Math.min(100.0, (annualDirectAndBattSelfConsumedKwh / annualPvKwh) * 100.0)
                : 0.0;

        double hardwareCost = (panelCount * panel.getPrice().doubleValue())
                + (battery != null ? batteryCount * battery.getPrice().doubleValue() : 0.0)
                + inverter.getPrice().doubleValue();
        double totalCapex = hardwareCost * 1.15; // Includes 15% BOS, mounting & installation

        double tariff = project.getTariffPerKwh().doubleValue();
        double feedIn = project.getFeedInPerKwh().doubleValue();
        double annualSavings = (annualDirectAndBattSelfConsumedKwh * tariff) + (annualGridExportKwh * feedIn);

        BigDecimal paybackYears = annualSavings > 0
                ? bd(totalCapex / annualSavings, 1)
                : null;

        savedSimulation.setAnnualPvKwh(bd(annualPvKwh, 1));
        savedSimulation.setSelfConsumptionPct(bd(selfConsumptionPct, 2));
        savedSimulation.setGridImportKwh(bd(annualGridImportKwh, 1));
        savedSimulation.setGridExportKwh(bd(annualGridExportKwh, 1));
        savedSimulation.setUnmetLoadKwh(bd(annualUnmetKwh, 1));
        savedSimulation.setCapex(bd(totalCapex, 2));
        savedSimulation.setAnnualSavings(bd(annualSavings, 2));
        savedSimulation.setPaybackYears(paybackYears);

        Simulation finalSimulation = simulationRepository.save(savedSimulation);
        return SimulationResponse.from(finalSimulation);
    }

    @Transactional(readOnly = true)
    public List<SimulationResponse> getProjectSimulations(User currentUser, UUID projectId) {
        if (projectRepository.findByIdAndUserId(projectId, currentUser.getId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }
        return simulationRepository.findByProjectIdOrderByRunAtDesc(projectId)
                .stream()
                .map(SimulationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public SimulationResponse getSimulationById(User currentUser, UUID simulationId) {
        Simulation sim = simulationRepository.findByIdAndProjectUserId(simulationId, currentUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Simulation not found"));
        return SimulationResponse.from(sim);
    }

    @Transactional(readOnly = true)
    public List<SimulationHourlyDto> getSimulationHourly(User currentUser, UUID simulationId, Short month) {
        if (simulationRepository.findByIdAndProjectUserId(simulationId, currentUser.getId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Simulation not found");
        }
        List<SimulationHourly> rows = (month == null)
                ? simulationHourlyRepository.findByIdSimulationIdOrderByIdMonthAscIdHourAsc(simulationId)
                : simulationHourlyRepository.findByIdSimulationIdAndIdMonthOrderByIdHourAsc(simulationId, month);

        return rows.stream().map(SimulationHourlyDto::from).toList();
    }

    @Transactional
    public void deleteSimulation(User currentUser, UUID simulationId) {
        Simulation sim = simulationRepository.findByIdAndProjectUserId(simulationId, currentUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Simulation not found"));
        simulationRepository.delete(sim);
    }

    // ==================== HELPER METHODS ====================

    private Panel resolvePanel(Project project) {
        if (project.getPanel() != null) {
            return project.getPanel();
        }
        return panelRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No solar panels available in catalog"));
    }

    private Battery resolveBattery(Project project) {
        if (project.getBattery() != null) {
            return project.getBattery();
        }
        List<Battery> all = batteryRepository.findAll();
        return all.stream()
                .filter(b -> "LFP".equalsIgnoreCase(b.getChemistry()))
                .findFirst()
                .orElseGet(() -> all.stream()
                        .findFirst()
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No batteries available in catalog")));
    }

    private Inverter resolveInverter(String mode, double peakRunningW, double peakSurgeW, Long requestedInverterId) {
        if (requestedInverterId != null) {
            return inverterRepository.findById(requestedInverterId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested inverter not found"));
        }

        List<Inverter> byMode = inverterRepository.findByType(mode);
        final List<Inverter> candidates = byMode.isEmpty() ? inverterRepository.findAll() : byMode;

        if (candidates.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No inverters available in catalog");
        }

        double requiredContinuous = peakRunningW * 1.15; // 15% headroom

        return candidates.stream()
                .filter(inv -> inv.getContinuousW() >= requiredContinuous && inv.getSurgeW() >= peakSurgeW)
                .min(Comparator.comparingInt(Inverter::getContinuousW))
                .orElseGet(() -> candidates.stream()
                        .max(Comparator.comparingInt(Inverter::getContinuousW))
                        .orElseThrow());
    }

    private double[] build24HourLoadProfile(Project project, double dailyLoadKwh) {
        double[] profileW = new double[24];

        if ("MANUAL".equalsIgnoreCase(project.getLoadInputMethod())) {
            double dailyWh = dailyLoadKwh * 1000.0;
            for (int h = 0; h < 24; h++) {
                profileW[h] = dailyWh * MANUAL_HOURLY_WEIGHTS[h];
            }
            return profileW;
        }

        for (ProjectDevice pd : project.getDevices()) {
            double avgRunningW = pd.getDevice().getTypicalWatts().doubleValue()
                    * pd.getQuantity()
                    * pd.getDevice().getDutyCycle().doubleValue();

            double hours = pd.getHoursPerDay().doubleValue();
            if (Boolean.TRUE.equals(pd.getAlwaysOn()) || hours >= 24.0) {
                for (int h = 0; h < 24; h++) {
                    profileW[h] += avgRunningW;
                }
            } else {
                int start = pd.getStartHour() != null ? pd.getStartHour() : 8;
                int fullHours = (int) Math.floor(hours);
                double fractionalHour = hours - fullHours;

                for (int offset = 0; offset < fullHours; offset++) {
                    profileW[(start + offset) % 24] += avgRunningW;
                }
                if (fractionalHour > 0.0) {
                    profileW[(start + fullHours) % 24] += avgRunningW * fractionalHour;
                }
            }
        }
        return profileW;
    }

    private Map<String, Object> buildAssumptionsMap(
            Panel panel, Battery battery, Inverter inverter,
            double designPsh, double annualAvgPsh, double worstMonthPsh, double performanceRatio
    ) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("panelModel", panel.getBrand() + " " + panel.getModel());
        map.put("panelRatedWatts", panel.getRatedWatts());
        map.put("batteryModel", battery != null ? battery.getBrand() + " " + battery.getModel() : "None (Grid-Tied)");
        map.put("inverterModel", inverter.getBrand() + " " + inverter.getModel());
        map.put("designPshHours", Math.round(designPsh * 100.0) / 100.0);
        map.put("annualAvgPshHours", Math.round(annualAvgPsh * 100.0) / 100.0);
        map.put("worstMonthPshHours", Math.round(worstMonthPsh * 100.0) / 100.0);
        map.put("performanceRatio", Math.round(performanceRatio * 1000.0) / 1000.0);
        map.put("bosMarkupPct", 15.0);
        map.put("noctTempC", 45.0);
        return map;
    }

    private BigDecimal bd(double value, int scale) {
        return BigDecimal.valueOf(value).setScale(scale, RoundingMode.HALF_UP);
    }
}