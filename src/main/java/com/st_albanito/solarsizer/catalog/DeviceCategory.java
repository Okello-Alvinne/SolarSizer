package com.st_albanito.solarsizer.catalog;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "device_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(nullable = false,unique = true,length = 60)
    private String name;

    @Column(nullable = false,length = 40)
    private String icon;
}
