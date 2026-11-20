package com.st_albanito.solarsizer.irradiance;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class IrradianceController {

    private final IrradianceService irradianceService;

    @GetMapping
    public ResponseEntity<List<LocationResponse>> getLocations(
            @RequestParam(name = "query", required = false) String query
    ) {
        return ResponseEntity.ok(irradianceService.getLocations(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponse> getLocationById(
            @PathVariable(name = "id") Long id
    ) {
        return ResponseEntity.ok(irradianceService.getLocationById(id));
    }

    @GetMapping("/{id}/hourly")
    public ResponseEntity<List<HourlyIrradianceDto>> getHourlyProfile(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "month", required = false) Short month
    ) {
        return ResponseEntity.ok(irradianceService.getHourlyProfile(id, month));
    }

    @PostMapping
    public ResponseEntity<LocationResponse> createLocation(
            @Valid @RequestBody CreateLocationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(irradianceService.createLocation(request));
    }
}