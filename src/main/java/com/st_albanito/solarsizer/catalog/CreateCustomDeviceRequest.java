package com.st_albanito.solarsizer.catalog;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateCustomDeviceRequest(
        @NotNull(message = "Category ID is required")
        Short categoryId,

        @NotBlank(message = "Device name is required")
        @Size(max = 120, message = "Device name cannot exceed 120 characters")
        String name,

        @NotNull(message = "Typical watts is required")
        @DecimalMin(value = "0.1", message = "Watts must be greater than 0")
        BigDecimal typicalWatts,

        @DecimalMin(value = "1.0", message = "Surge multiplier must be at least 1.0")
        @DecimalMax(value = "10.0", message = "Surge multiplier cannot exceed 10.0")
        BigDecimal surgeMultiplier,

        @DecimalMin(value = "0.05", message = "Duty cycle must be between 0.05 and 1.00")
        @DecimalMax(value = "1.00", message = "Duty cycle must be between 0.05 and 1.00")
        BigDecimal dutyCycle,

        @NotNull(message = "Default hours per day is required")
        @DecimalMin(value = "0.1", message = "Hours per day must be between 0.1 and 24.0")
        @DecimalMax(value = "24.0", message = "Hours per day cannot exceed 24.0")
        BigDecimal defaultHoursPerDay,

        String sourceNote
) {}