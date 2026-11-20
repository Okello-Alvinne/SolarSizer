package com.st_albanito.solarsizer.irradiance;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IrradianceTypicalDayRepository extends JpaRepository<IrradianceTypicalDay, IrradianceTypicalDayId> {

    List<IrradianceTypicalDay> findByIdLocationIdOrderByIdMonthAscIdHourAsc(Long locationId);

    List<IrradianceTypicalDay> findByIdLocationIdAndIdMonthOrderByIdHourAsc(Long locationId, Short month);
}