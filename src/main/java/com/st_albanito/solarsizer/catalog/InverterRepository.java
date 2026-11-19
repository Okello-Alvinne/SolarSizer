package com.st_albanito.solarsizer.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InverterRepository extends JpaRepository<Inverter, Long> {
    List<Inverter> findByTypeIgnoreCase(String type);
}