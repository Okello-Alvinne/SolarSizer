package com.st_albanito.solarsizer.simulation;

import jakarta.validation.constraints.Size;

public record RunSimulationRequest(
        @Size(max = 80, message = "Label must be at most 80 characters")
        String label,

        Long inverterId
) {}