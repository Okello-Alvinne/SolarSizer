package com.st_albanito.solarsizer.project;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ProjectResponse (
        UUID id,
        String name,
        String systemMode,
        Long locationId,
        String locationName,
        String loadInputMethod,
        BigDecimal manualDailyKwh,
        BigDecimal autonomyDays,
        BigDecimal backupHoursTarget,
        Long outageProfileId,
        BigDecimal tariffPerKwh,
        BigDecimal feedInPerKwh,
        Long panelId,
        Long batteryId,
        BigDecimal tiltDeg,
        BigDecimal azimuthDeg,
        BigDecimal systemLossesPct,
        BigDecimal calculatedDailyLoadKwh,
        BigDecimal peakRunningWatts,
        BigDecimal peakSurgeWatts,
        List<ProjectDeviceResponse> devices,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
){
    public static ProjectResponse from(Project p){
        List<ProjectDeviceResponse> deviceDtos = p.getDevices().stream()
                .map(ProjectDeviceResponse::from)
                .toList();

        BigDecimal totalRunningW = deviceDtos.stream()
                .map(ProjectDeviceResponse::runningWatts)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal maxSurgeIncrement = deviceDtos.stream()
                .map(d-> d.surgeWatts().subtract(d.runningWatts()))
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        BigDecimal peakSurgeW = totalRunningW.add(maxSurgeIncrement);
        BigDecimal dailyLoadKwh;
        if("MANUAL".equalsIgnoreCase(p.getLoadInputMethod())&& p.getManualDailyKwh()!=null){
            dailyLoadKwh = p.getManualDailyKwh().setScale(2, RoundingMode.HALF_UP);
        }else{
            BigDecimal totalWh = deviceDtos.stream()
                    .map(ProjectDeviceResponse::dailyEnergyWh)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            dailyLoadKwh = totalWh.divide(BigDecimal.valueOf(1000),2,RoundingMode.HALF_UP);
        }

        return new ProjectResponse(
                p.getId(),
                p.getName(),
                p.getSystemMode(),
                p.getLocation().getId(),
                p.getLocation().getName(),
                p.getLoadInputMethod(),
                p.getManualDailyKwh(),
                p.getAutonomyDays(),
                p.getBackupHoursTarget(),
                p.getOutageProfile() != null ? p.getOutageProfile().getId() : null,
                p.getTariffPerKwh(),
                p.getFeedInPerKwh(),
                p.getPanel() != null ? p.getPanel().getId() : null,
                p.getBattery() != null ? p.getBattery().getId() : null,
                p.getTiltDeg(),
                p.getAzimuthDeg(),
                p.getSystemLossesPct(),
                dailyLoadKwh,
                totalRunningW.setScale(1, RoundingMode.HALF_UP),
                peakSurgeW,
                deviceDtos,
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}
