package com.st_albanito.solarsizer.irradiance;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "locations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 60)
    private String country;

    @Column(nullable = false, precision = 8, scale = 5)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 8, scale = 5)
    private BigDecimal longitude;

    @Column(nullable = false, length = 50)
    private String timezone;
}
