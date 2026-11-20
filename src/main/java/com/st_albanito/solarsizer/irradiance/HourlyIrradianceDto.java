package com.st_albanito.solarsizer.irradiance;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record HourlyIrradianceDto(
        @NotNull @Min(1) @Max(12)
        Short month,

        @NotNull @Min(0) @Max(23)
        Short hour,

        @NotNull @DecimalMin("0.0") @DecimalMax("1500.0")
        BigDecimal ghiWM2,

        @NotNull @DecimalMin("-30.0") @DecimalMax("60.0")
        BigDecimal tempC
) {
    public static HourlyIrradianceDto from(IrradianceTypicalDay entity) {
        return new HourlyIrradianceDto(
                entity.getId().getMonth(),
                entity.getId().getHour(),
                entity.getGhiWM2(),
                entity.getTempC()
        );
    }
}