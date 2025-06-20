package com.hospital.appointmentservice.admin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "department_id", nullable = false)
    private UUID id;

    @Nationalized
    @Lob
    @Column(name = "name")
    private String name;

    @Size(max = 50)
    @Nationalized
    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "status")
    private Boolean status;

    @OneToMany(mappedBy = "department")
    private Set<com.hospital.appointmentservice.admin.model.Room> rooms = new LinkedHashSet<>();

    @OneToMany(mappedBy = "department")
    private Set<com.hospital.appointmentservice.admin.model.Staff> staff = new LinkedHashSet<>();

}