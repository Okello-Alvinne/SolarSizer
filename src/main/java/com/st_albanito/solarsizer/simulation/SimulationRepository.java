package com.st_albanito.solarsizer.simulation;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SimulationRepository extends JpaRepository<Simulation, UUID> {

    List<Simulation> findByProjectIdOrderByRunAtDesc(UUID projectId);

    Optional<Simulation> findByIdAndProjectUserId(UUID id, UUID userId);
}