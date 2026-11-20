package com.st_albanito.solarsizer.irradiance;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IrradianceService {

    private final LocationRepository locationRepository;
    private final IrradianceTypicalDayRepository irradianceRepository;

    @Transactional(readOnly = true)
    public List<LocationResponse> getLocations(String query) {
        String cleanQuery = (query == null) ? "" : query.trim();
        return locationRepository.searchLocations(cleanQuery).stream()
                .map(this::buildLocationResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LocationResponse getLocationById(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found"));
        return buildLocationResponse(location);
    }

    @Transactional(readOnly = true)
    public List<HourlyIrradianceDto> getHourlyProfile(Long locationId, Short month) {
        if (!locationRepository.existsById(locationId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found");
        }
        List<IrradianceTypicalDay> rows = (month == null)
                ? irradianceRepository.findByIdLocationIdOrderByIdMonthAscIdHourAsc(locationId)
                : irradianceRepository.findByIdLocationIdAndIdMonthOrderByIdHourAsc(locationId, month);

        return rows.stream().map(HourlyIrradianceDto::from).toList();
    }

    @Transactional
    public LocationResponse createLocation(CreateLocationRequest request) {
        Set<String> seenSlots = new HashSet<>();
        for (HourlyIrradianceDto h : request.hourlyData()) {
            if (!seenSlots.add(h.month() + ":" + h.hour())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Duplicate month/hour slot: month " + h.month() + ", hour " + h.hour()
                );
            }
        }

        Location savedLocation = locationRepository.save(Location.builder()
                .name(request.name().trim())
                .country(request.country().trim())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .timezone(request.timezone().trim())
                .build());

        List<IrradianceTypicalDay> rows = request.hourlyData().stream()
                .map(h -> IrradianceTypicalDay.builder()
                        .id(new IrradianceTypicalDayId(savedLocation.getId(), h.month(), h.hour()))
                        .location(savedLocation)
                        .ghiWM2(h.ghiWM2())
                        .tempC(h.tempC())
                        .build())
                .toList();

        irradianceRepository.saveAll(rows);
        return buildLocationResponse(savedLocation);
    }

    private LocationResponse buildLocationResponse(Location location) {
        List<IrradianceTypicalDay> rows =
                irradianceRepository.findByIdLocationIdOrderByIdMonthAscIdHourAsc(location.getId());

        Map<Short, List<IrradianceTypicalDay>> byMonth = rows.stream()
                .collect(Collectors.groupingBy(r -> r.getId().getMonth(), TreeMap::new, Collectors.toList()));

        List<MonthlyIrradianceSummaryDto> summaries = new ArrayList<>();
        for (Map.Entry<Short, List<IrradianceTypicalDay>> entry : byMonth.entrySet()) {
            List<IrradianceTypicalDay> monthRows = entry.getValue();

            BigDecimal totalWhM2 = monthRows.stream()
                    .map(IrradianceTypicalDay::getGhiWM2)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Wh/m2/day divided by 1000 = kWh/m2/day = Peak Sun Hours (PSH)
            BigDecimal psh = totalWhM2.divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP);

            BigDecimal avgTemp = monthRows.stream()
                    .map(IrradianceTypicalDay::getTempC)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(monthRows.size()), 1, RoundingMode.HALF_UP);

            summaries.add(new MonthlyIrradianceSummaryDto(entry.getKey(), psh, avgTemp));
        }

        BigDecimal annualAvgPsh = BigDecimal.ZERO;
        BigDecimal worstMonthPsh = BigDecimal.ZERO;

        if (!summaries.isEmpty()) {
            annualAvgPsh = summaries.stream()
                    .map(MonthlyIrradianceSummaryDto::pshHours)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(summaries.size()), 2, RoundingMode.HALF_UP);

            worstMonthPsh = summaries.stream()
                    .map(MonthlyIrradianceSummaryDto::pshHours)
                    .min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);
        }

        return new LocationResponse(
                location.getId(),
                location.getName(),
                location.getCountry(),
                location.getLatitude(),
                location.getLongitude(),
                location.getTimezone(),
                annualAvgPsh,
                worstMonthPsh,
                summaries
        );
    }
}