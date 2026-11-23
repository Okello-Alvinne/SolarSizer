package com.st_albanito.solarsizer.simulation;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode //??
public class SimulationHourlyId implements Serializable {

    @Column(name = "simulation_id")
    private UUID simulationId;

    @Column(name = "month")
    private Short month;

    @Column(name = "hour")
    private Short hour;
}