package com.st_albanito.solarsizer.project;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record ProjectDeviceResponse (
        Long id,
        Long deviceId,
        String deviceName,
        BigDecimal typicalWatts,
        BigDecimal surgeMultiplier,
        BigDecimal dutyCycle,
        Short quantity,
        BigDecimal hoursPerDay,
        Short startHour,
        Boolean alwaysOn,
        BigDecimal dailyEnergyWh,
        BigDecimal runningWatts,
        BigDecimal surgeWatts
){
    public static ProjectDeviceResponse from(ProjectDevice pd){
        BigDecimal qty = BigDecimal.valueOf(pd.getQuantity());
        BigDecimal watts = pd.getDevice().getTypicalWatts();
        BigDecimal duty = pd.getDevice().getDutyCycle();
        BigDecimal surgeMult = pd.getDevice().getSurgeMultiplier();

        BigDecimal runningW = watts.multiply(qty).setScale(1, RoundingMode.HALF_UP);
        BigDecimal surgeW = runningW.multiply(surgeMult).setScale(1, RoundingMode.HALF_UP);
        BigDecimal dailyWh = runningW.multiply(duty).multiply(pd.getHoursPerDay().setScale(1,RoundingMode.HALF_UP));

        return new ProjectDeviceResponse(
                pd.getId(),
                pd.getDevice().getId(),
                pd.getDevice().getName(),
                watts,
                surgeMult,
                duty,
                pd.getQuantity(),
                pd.getHoursPerDay(),
                pd.getStartHour(),
                pd.getAlwaysOn(),
                dailyWh,
                runningW,
                surgeW
        );
    }
}
