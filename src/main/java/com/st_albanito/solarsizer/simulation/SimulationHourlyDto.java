package com.st_albanito.solarsizer.simulation;

import java.math.BigDecimal;

public record SimulationHourlyDto(
        Short month,
        Short hour,
        BigDecimal loadW,
        BigDecimal pvW,
        BigDecimal batterySocPct,
        BigDecimal gridImportW,
        BigDecimal gridExportW,
        BigDecimal unmetW
) {
    public static SimulationHourlyDto from(SimulationHourly entity){
        return new SimulationHourlyDto(
                entity.getId().getMonth(),
                entity.getId().getHour(),
                entity.getLoadW(),
                entity.getPvW(),
                entity.getBatterySocPct(),
                entity.getGridImportW(),
                entity.getGridExportW(),
                entity.getUnmetW()
        );
    }
}
