package com.st_albanito.solarsizer.simulation;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "simulation_hourly")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimulationHourly {

    @EmbeddedId
    private SimulationHourlyId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("simulationId")
    @JoinColumn(name = "simulation_id", nullable = false)
    private Simulation simulation;

    @Column(name = "load_w", nullable = false, precision = 9, scale = 1)
    private BigDecimal loadW;

    @Column(name = "pv_w", nullable = false, precision = 9, scale = 1)
    private BigDecimal pvW;

    @Column(name = "battery_soc_pct", nullable = false, precision = 5, scale = 2)
    private BigDecimal batterySocPct;

    @Column(name = "grid_import_w", nullable = false, precision = 9, scale = 1)
    private BigDecimal gridImportW;

    @Column(name = "grid_export_w", nullable = false, precision = 9, scale = 1)
    private BigDecimal gridExportW;

    @Column(name = "unmet_w", nullable = false, precision = 9, scale = 1)
    private BigDecimal unmetW;
}