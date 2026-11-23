package com.st_albanito.solarsizer.simulation;

import com.st_albanito.solarsizer.project.Project;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "simulations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Simulation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false, length = 80)
    private String label;

    @Column(nullable = false, length = 12)
    private String status; // COMPLETED, FAILED

    @Column(name = "run_at", nullable = false, updatable = false)
    private LocalDateTime runAt;

    @Column(name = "daily_load_kwh", nullable = false, precision = 8, scale = 2)
    private BigDecimal dailyLoadKwh;

    @Column(name = "peak_load_w", nullable = false, precision = 9, scale = 1)
    private BigDecimal peakLoadW;

    @Column(name = "pv_kwp", nullable = false, precision = 7, scale = 2)
    private BigDecimal pvKwp;

    @Column(name = "panel_count", nullable = false)
    private Integer panelCount;

    @Column(name = "battery_kwh", nullable = false, precision = 8, scale = 2)
    private BigDecimal batteryKwh;

    @Column(name = "battery_count", nullable = false)
    private Integer batteryCount;

    @Column(name = "inverter_continuous_w", nullable = false)
    private Integer inverterContinuousW;

    @Column(name = "inverter_surge_w", nullable = false)
    private Integer inverterSurgeW;

    @Column(name = "controller_amps", nullable = false, precision = 6, scale = 1)
    private BigDecimal controllerAmps;

    @Column(name = "annual_pv_kwh", nullable = false, precision = 10, scale = 1)
    private BigDecimal annualPvKwh;

    @Column(name = "self_consumption_pct", nullable = false, precision = 5, scale = 2)
    private BigDecimal selfConsumptionPct;

    @Column(name = "grid_import_kwh", nullable = false, precision = 10, scale = 1)
    private BigDecimal gridImportKwh;

    @Column(name = "grid_export_kwh", nullable = false, precision = 10, scale = 1)
    private BigDecimal gridExportKwh;

    @Column(name = "unmet_load_kwh", nullable = false, precision = 10, scale = 1)
    @Builder.Default
    private BigDecimal unmetLoadKwh = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal capex;

    @Column(name = "annual_savings", nullable = false, precision = 12, scale = 2)
    private BigDecimal annualSavings;

    @Column(name = "payback_years", precision = 5, scale = 1)
    private BigDecimal paybackYears;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "assumptions_json", columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> assumptionsJson;

    @PrePersist
    protected void onCreate() {
        if (this.runAt == null) {
            this.runAt = LocalDateTime.now();
        }
    }
}