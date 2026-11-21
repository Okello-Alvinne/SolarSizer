package com.st_albanito.solarsizer.project;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProjectRequest(
        @NotBlank(message = "Project name is required")
        @Size(max = 120)
        String name,

        @NotBlank(message = "systemMode is required (ON_GRID, OFF_GRID, HYBRID)")
        @Pattern(regexp = "(?i)ON_GRID|OFF_GRID|HYBRID", message = "systemMode must be ON_GRID, OFF_GRID, or HYBRID")
        String systemMode,
//new
        @NotNull(message = "locationId is required")
        Long locationId,

        @NotBlank(message = "loadInputMethod is required (MANUAL or DEVICES)")
        @Pattern(regexp = "(?i)MANUAL|DEVICES", message = "loadInputMethod must be MANUAL or DEVICES")
        String loadInputMethod,

        @DecimalMin("0.1")
        BigDecimal manualDailyKwh,

        @DecimalMin("0.5") @DecimalMax("14.0")
        BigDecimal autonomyDays,

        @DecimalMin("0.5") @DecimalMax("72.0")
        BigDecimal backupHoursTarget,

        Long outageProfileId,

        @NotNull(message = "tariffPerKwh is required")
        @DecimalMin("0.0")
        BigDecimal tariffPerKwh,

        @DecimalMin("0.0")
        BigDecimal feedInPerKwh,

        Long panelId,

        Long batteryId,

        @DecimalMin("0.0") @DecimalMax("90.0")
        BigDecimal tiltDeg,

        @DecimalMin("-180.0") @DecimalMax("180.0")
        BigDecimal azimuthDeg,

        @DecimalMin("0.0") @DecimalMax("50.0")
        BigDecimal systemLossesPct
) {}