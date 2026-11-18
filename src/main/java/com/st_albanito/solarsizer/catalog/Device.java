package com.st_albanito.solarsizer.catalog;

import com.st_albanito.solarsizer.auth.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "devices")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "category_id",nullable = false)
    private DeviceCategory category;

    @Column(nullable = false,length = 120)
    private String name;

    @Column(nullable = false,name = "typical_watts",precision = 8,scale = 1)
    private BigDecimal typicalWatts;
    @Column(name = "surge_multiplier",nullable = false,precision = 3,scale = 1)
    @Builder.Default
    private BigDecimal surgeMultiplier = BigDecimal.valueOf(1.0);

    @Column(name = "duty_cycle",nullable = false,precision = 3,scale = 2)
    @Builder.Default
    private BigDecimal dutyCycle = BigDecimal.valueOf(1.00);

    @Column(name = "default_hours_per_day",nullable = false,precision = 4,scale = 1)
    private BigDecimal defaultHoursPerDay;

    @Column(name = "source_note",columnDefinition = "TEXT")
    private String sourceNote;
    @Column(name = "is_custom",nullable = false)
    @Builder.Default
    private boolean isCustom = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
}
