package com.st_albanito.solarsizer.simulation;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SimulationHourlyRepository extends JpaRepository<SimulationHourly, SimulationHourlyId> {

    List<SimulationHourly> findByIdSimulationIdOrderByIdMonthAscIdHourAsc(UUID simulationId);

    List<SimulationHourly> findByIdSimulationIdAndIdMonthOrderByIdHourAsc(UUID simulationId, Short month);

    void deleteByIdSimulationId(UUID simulationId);
}