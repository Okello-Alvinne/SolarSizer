package com.st_albanito.solarsizer.catalog;

import com.st_albanito.solarsizer.auth.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CatalogController {
    private final CatalogService catalogService;

    @GetMapping("/devices/categories")
    public ResponseEntity<List<DeviceResponse>> getCategories(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(name = "categoryId", required = false) Short categoryId,
            @RequestParam(name = "query", required = false) String query
    ){
        return ResponseEntity.ok(catalogService.getDevices(currentUser,categoryId,query));
    }
    @GetMapping("/devices")
    public ResponseEntity<List<DeviceResponse>> getDevices(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) Short categoryId,
            @RequestParam(required = false ) String query
    ){
        return ResponseEntity.ok(catalogService.getDevices(currentUser,categoryId,query));
    }

    @PostMapping("/devices")
    public ResponseEntity<DeviceResponse> createCustomDevice(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody CreateCustomDeviceRequest request
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogService.createCustomDevice(currentUser,request));
    }

    @GetMapping("/catalog/panels")
    public ResponseEntity<List<Panel>> getPanels() {
        return ResponseEntity.ok(catalogService.getAllPanels());
    }

    @GetMapping("/catalog/batteries")
    public ResponseEntity<List<Battery>> getBatteries() {
        return ResponseEntity.ok(catalogService.getAllBatteries());
    }

    @GetMapping("/catalog/inverters")
    public ResponseEntity<List<Inverter>> getInverters(
            @RequestParam(name = "type", required = false) String type
    ) {
        return ResponseEntity.ok(catalogService.getInverters(type));
    }
    @GetMapping("/catalog/outage-profiles")
    public ResponseEntity<List<GridOutageProfile>> getOutageProfiles() {
        return ResponseEntity.ok(catalogService.getPresetGridOutageProfiles());
    }
}
