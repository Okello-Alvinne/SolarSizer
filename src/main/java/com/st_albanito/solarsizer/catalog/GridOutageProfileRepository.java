package com.st_albanito.solarsizer.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GridOutageProfileRepository extends JpaRepository<GridOutageProfile, Long> {
    List<GridOutageProfile> findByIsPresetTrue();
}