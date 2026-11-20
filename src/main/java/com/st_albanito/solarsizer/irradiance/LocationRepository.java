package com.st_albanito.solarsizer.irradiance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("""
        SELECT l FROM Location l
        WHERE LOWER(l.name) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(l.country) LIKE LOWER(CONCAT('%', :search, '%'))
        ORDER BY l.country ASC, l.name ASC
        """)
    List<Location> searchLocations(@Param("search") String search);
}