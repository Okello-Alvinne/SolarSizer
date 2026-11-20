package com.st_albanito.solarsizer.irradiance;

import java.math.BigDecimal;
import java.util.List;

public record LocationResponse(
        Long id,
        String name,
        String country,
        BigDecimal latitude,
        BigDecimal longitude,
        String timezone,
        BigDecimal annualAvgPsh,
        BigDecimal worstMonthPsh,
        List<MonthlyIrradianceSummaryDto> monthlySummaries
) {}