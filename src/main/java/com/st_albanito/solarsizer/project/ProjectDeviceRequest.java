package com.st_albanito.solarsizer.project;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProjectDeviceRequest(
        @NotNull(message = "deviceId is required")
        Long deviceId,

        @NotNull @Min(1) @Max(500)
        Short quantity,

        @NotNull @DecimalMin("0.1") @DecimalMax("24.0")
        BigDecimal hoursPerDay,

        @Min(0) @Max(23)
        Short startHour,

        Boolean alwaysOn
) {}