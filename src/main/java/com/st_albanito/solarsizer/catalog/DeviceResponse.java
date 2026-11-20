package com.st_albanito.solarsizer.catalog;

import java.math.BigDecimal;

public record DeviceResponse (
        Long id,
        Short categoryId,
        String categoryName,
        String categoryIcon,
        String name,
        BigDecimal typicalWatts,
        BigDecimal surgeMultiplier,
        BigDecimal dutyCycle,
        BigDecimal defaultHoursPerDay,
        String sourceNote,
        Boolean isCustom
){
    public static DeviceResponse from(Device device){
        return new DeviceResponse(
                device.getId(),
                device.getCategory().getId(),
                device.getCategory().getName(),
                device.getCategory().getIcon(),
                device.getName(),
                device.getTypicalWatts(),
                device.getSurgeMultiplier(),
                device.getDutyCycle(),
                device.getDefaultHoursPerDay(),
                device.getSourceNote(),
                device.isCustom()
        );
    }
}
