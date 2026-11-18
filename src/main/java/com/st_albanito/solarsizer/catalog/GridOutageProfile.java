package com.st_albanito.solarsizer.catalog;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "grid_outage_profiles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GridOutageProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,length = 80)
    private String name;
    @Column(name = "outages_per_month",nullable = false)
    private Short outagesPerMonth;
    @Column(name = "avg_duration_hours",nullable = false,precision = 4,scale = 1)
    private BigDecimal avgDurationHours;
    @Column(name = "typical_start_hour",nullable = false)
    private Short typicalStartHour;
    @Column(name = "is_preset",nullable = false)
    @Builder.Default
    private Boolean isPreset = false;
}
