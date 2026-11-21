package com.st_albanito.solarsizer.project;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ProjectDeviceRepository extends JpaRepository<ProjectDevice, Long> {

    Optional<ProjectDevice> findByIdAndProjectId(Long id, UUID projectId);
}