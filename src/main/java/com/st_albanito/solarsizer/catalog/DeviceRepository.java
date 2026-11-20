package com.st_albanito.solarsizer.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    @Query("""
        SELECT d FROM Device d
        WHERE (d.isCustom = false OR d.createdBy.id = :userId)
          AND LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%'))
        ORDER BY d.category.id ASC, d.name ASC
        """)
    List<Device> searchAllAvailableDevices(
            @Param("userId") UUID userId,
            @Param("search") String search
    );

    @Query("""
        SELECT d FROM Device d
        WHERE (d.isCustom = false OR d.createdBy.id = :userId)
          AND d.category.id = :categoryId
          AND LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%'))
        ORDER BY d.category.id ASC, d.name ASC
        """)
    List<Device> searchAvailableDevicesByCategory(
            @Param("userId") UUID userId,
            @Param("categoryId") Short categoryId,
            @Param("search") String search
    );
}