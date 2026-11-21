package com.st_albanito.solarsizer.project;

import com.st_albanito.solarsizer.auth.User;
import com.st_albanito.solarsizer.catalog.Battery;
import com.st_albanito.solarsizer.catalog.GridOutageProfile;
import com.st_albanito.solarsizer.catalog.Panel;
import com.st_albanito.solarsizer.irradiance.Location;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "system_mode", nullable = false, length = 12)
    private String systemMode; // ON_GRID, OFF_GRID, HYBRID

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(name = "load_input_method", nullable = false, length = 10)
    private String loadInputMethod; // MANUAL, DEVICES

    @Column(name = "manual_daily_kwh", precision = 8, scale = 2)
    private BigDecimal manualDailyKwh;

    @Column(name = "autonomy_days", precision = 3, scale = 1)
    private BigDecimal autonomyDays;

    @Column(name = "backup_hours_target", precision = 4, scale = 1)
    private BigDecimal backupHoursTarget;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outage_profile_id")
    private GridOutageProfile outageProfile;

    @Column(name = "tariff_per_kwh", nullable = false, precision = 8, scale = 3)
    private BigDecimal tariffPerKwh;

    @Column(name = "feed_in_per_kwh", nullable = false, precision = 8, scale = 3)
    @Builder.Default
    private BigDecimal feedInPerKwh = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "panel_id")
    private Panel panel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "battery_id")
    private Battery battery;

    @Column(name = "tilt_deg", precision = 4, scale = 1)
    private BigDecimal tiltDeg;

    @Column(name = "azimuth_deg", nullable = false, precision = 5, scale = 1)
    @Builder.Default
    private BigDecimal azimuthDeg = BigDecimal.ZERO;

    @Column(name = "system_losses_pct", nullable = false, precision = 4, scale = 1)
    @Builder.Default
    private BigDecimal systemLossesPct = new BigDecimal("14.0");

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProjectDevice> devices = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}