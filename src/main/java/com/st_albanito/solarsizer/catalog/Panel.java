package com.st_albanito.solarsizer.catalog;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "panel_catalog")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Panel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 80)
    private String brand;
    @Column(nullable = false,length = 100)
    private String model;
    @Column(name = "rated_watts",nullable = false,precision = 7,scale = 1)
    private BigDecimal ratedWatts;  //precision,scale,fetch-types
    @Column(nullable = false,precision = 6,scale = 2)
    private BigDecimal vmp;
    @Column(nullable = false,precision = 6,scale = 2)
    private BigDecimal voc;
    @Column(nullable = false,precision = 6,scale = 2)
    private BigDecimal imp;
    @Column(nullable = false,precision = 6,scale = 2)
    private BigDecimal isc;

    @Column(name = "temp_coeff_pmax",nullable = false,precision = 5,scale = 3)
    private BigDecimal tempCoeffPmax;
    @Column(name = "efficiency_pct",nullable = false,precision = 5,scale = 2)
    private BigDecimal efficiencyPct;
    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal price;

}
