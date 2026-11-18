package com.st_albanito.solarsizer.catalog;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "battery_catalog")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Battery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 80)
    private String brand;
    @Column(nullable = false,length = 100)
    private String model;
    @Column(nullable = false,length = 20)
    private String chemistry;
    @Column(nullable = false,name = "nominal_voltage",precision = 6,scale = 2)
    private BigDecimal nominalVoltage;
    @Column(nullable = false,name = "capacity_kwh", precision = 7,scale = 2)
    private BigDecimal capacityKwh;
    @Column(name = "usable_dod_pct", nullable = false,precision = 5,scale = 2)
    private BigDecimal usableDodPct;
    @Column(name = "round_trip_eff_pct", nullable = false,precision = 5,scale = 2)
    private BigDecimal roundTripEffPct;

    @Column(name = "cycle_life",nullable = false)
    private Integer cycleLife;
    @Column(name = "annual_fade_pct",nullable = false,precision = 4,scale = 2)
    private BigDecimal annualFadePct;
    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal price;
}
