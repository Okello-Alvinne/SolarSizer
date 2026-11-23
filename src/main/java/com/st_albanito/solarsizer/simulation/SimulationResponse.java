package com.st_albanito.solarsizer.simulation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record SimulationResponse(
        UUID id,
        UUID projectId,
        String projectName,
        String label,
        String status,
        LocalDateTime runAt,
        BigDecimal dailyLoadKwh,
        BigDecimal peakLoadW,
        BigDecimal pvKwp,
        Integer panelCount,
        BigDecimal batteryKwh,
        Integer batteryCount,
        Integer inverterContinuousW,
        Integer inverterSurgeW,
        BigDecimal controllerAmps,
        BigDecimal annualPvKwh,
        BigDecimal selfConsumptionPct,
        BigDecimal gridImportKwh,
        BigDecimal gridExportKwh,
        BigDecimal unmetLoadKwh,
        BigDecimal capex,
        BigDecimal annualSavings,
        BigDecimal paybackYears,
        Map<String, Object> assumptionsJson
) {
    public static SimulationResponse from(Simulation s) {
        return new SimulationResponse(
                s.getId(),
                s.getProject().getId(),
                s.getProject().getName(),
                s.getLabel(),
                s.getStatus(),
                s.getRunAt(),
                s.getDailyLoadKwh(),
                s.getPeakLoadW(),
                s.getPvKwp(),
                s.getPanelCount(),
                s.getBatteryKwh(),
                s.getBatteryCount(),
                s.getInverterContinuousW(),
                s.getInverterSurgeW(),
                s.getControllerAmps(),
                s.getAnnualPvKwh(),
                s.getSelfConsumptionPct(),
                s.getGridImportKwh(),
                s.getGridExportKwh(),
                s.getUnmetLoadKwh(),
                s.getCapex(),
                s.getAnnualSavings(),
                s.getPaybackYears(),
                s.getAssumptionsJson()
        );
    }
}
