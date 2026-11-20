package com.st_albanito.solarsizer.irradiance;

import java.math.BigDecimal;

public record MonthlyIrradianceSummaryDto(
        Short month,
        BigDecimal pshHours,
        BigDecimal avgTempC
) {}