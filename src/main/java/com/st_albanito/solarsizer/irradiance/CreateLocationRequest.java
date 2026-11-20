package com.st_albanito.solarsizer.irradiance;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record CreateLocationRequest(
        @NotBlank @Size(max = 120)
        String name,

        @NotBlank @Size(max = 60)
        String country,

        @NotNull @DecimalMin("-90.0") @DecimalMax("90.0")
        BigDecimal latitude,

        @NotNull @DecimalMin("-180.0") @DecimalMax("180.0")
        BigDecimal longitude,

        @NotBlank @Size(max = 50)
        String timezone,

        @NotEmpty
        @Size(min = 288, max = 288, message = "Exactly 288 hourly records (12 months x 24 hours) are required")
        @Valid
        List<HourlyIrradianceDto> hourlyData
) {}