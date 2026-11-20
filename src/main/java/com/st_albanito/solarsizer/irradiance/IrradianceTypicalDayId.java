package com.st_albanito.solarsizer.irradiance;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class IrradianceTypicalDayId implements Serializable {

    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "month")
    private Short month;

    @Column(name = "hour")
    private Short hour;
}
//ask