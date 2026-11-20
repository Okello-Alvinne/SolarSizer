package com.st_albanito.solarsizer.irradiance;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "irradiance_typical_day")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IrradianceTypicalDay {

    @EmbeddedId
    private IrradianceTypicalDayId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("locationId")
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(name = "ghi_w_m2", nullable = false, precision = 7, scale = 2)
    private BigDecimal ghiWM2;

    @Column(name = "temp_c", nullable = false, precision = 4, scale = 1)
    private BigDecimal tempC;
}
//these