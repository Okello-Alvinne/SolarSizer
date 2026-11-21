package com.st_albanito.solarsizer.project;

import com.st_albanito.solarsizer.catalog.Device;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "project_devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(nullable = false)
    @Builder.Default
    private Short quantity = 1;

    @Column(name = "hours_per_day", nullable = false, precision = 4, scale = 1)
    private BigDecimal hoursPerDay;

    @Column(name = "start_hour")
    private Short startHour; // 0..23

    @Column(name = "always_on", nullable = false)
    @Builder.Default
    private Boolean alwaysOn = false;
}