package com.st_albanito.solarsizer.simulation;

import com.st_albanito.solarsizer.auth.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SimulationController {

    private final SimulationService simulationService;

    @PostMapping("/projects/{projectId}/simulations")
    public ResponseEntity<SimulationResponse> runSimulation(
            @AuthenticationPrincipal User currentUser,
            @PathVariable(name = "projectId") UUID projectId,
            @Valid @RequestBody(required = false) RunSimulationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(simulationService.runSimulation(currentUser, projectId, request));
    }

    @GetMapping("/projects/{projectId}/simulations")
    public ResponseEntity<List<SimulationResponse>> getProjectSimulations(
            @AuthenticationPrincipal User currentUser,
            @PathVariable(name = "projectId") UUID projectId
    ) {
        return ResponseEntity.ok(simulationService.getProjectSimulations(currentUser, projectId));
    }

    @GetMapping("/simulations/{simulationId}")
    public ResponseEntity<SimulationResponse> getSimulationById(
            @AuthenticationPrincipal User currentUser,
            @PathVariable(name = "simulationId") UUID simulationId
    ) {
        return ResponseEntity.ok(simulationService.getSimulationById(currentUser, simulationId));
    }

    @GetMapping("/simulations/{simulationId}/hourly")
    public ResponseEntity<List<SimulationHourlyDto>> getSimulationHourly(
            @AuthenticationPrincipal User currentUser,
            @PathVariable(name = "simulationId") UUID simulationId,
            @RequestParam(name = "month", required = false) Short month
    ) {
        return ResponseEntity.ok(simulationService.getSimulationHourly(currentUser, simulationId, month));
    }

    @DeleteMapping("/simulations/{simulationId}")
    public ResponseEntity<Void> deleteSimulation(
            @AuthenticationPrincipal User currentUser,
            @PathVariable(name = "simulationId") UUID simulationId
    ) {
        simulationService.deleteSimulation(currentUser, simulationId);
        return ResponseEntity.noContent().build();
    }
}