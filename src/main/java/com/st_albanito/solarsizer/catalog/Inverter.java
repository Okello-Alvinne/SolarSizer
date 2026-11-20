package com.st_albanito.solarsizer.catalog;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "inverter_catalog")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inverter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 80)
    private String brand;
    @Column(nullable = false,length = 100)
    private String model;
    @Column(nullable = false,length = 20)
    private String type;

    @Column(name="continuous_w",nullable = false)
    private Integer continuousW;
    @Column(name="surge_w",nullable = false)
    private Integer surgeW;

    @Column(nullable = false,name = "efficiency_pct",precision = 5,scale = 2)
    private BigDecimal efficiencyPct;
    @Column(nullable = false,name = "mppt_v_min", precision = 6,scale = 1)
    private BigDecimal mpptVMin;
    @Column(name = "mppt_v_max", nullable = false,precision = 6,scale = 1)
    private BigDecimal mpptVMax;
    @Column(name = "battery_voltage", nullable = false,precision = 6,scale = 2)
    private BigDecimal batteryVoltage;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal price;
}
