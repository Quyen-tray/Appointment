package com.hospital.appointmentservice.auth.model;

import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.admin.model.Staff;
import com.hospital.appointmentservice.receptionist.entity.Appointment;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false)
    private UUID id;

    @Nationalized
    @Lob
    @Column(name = "username")
    private String username;

    @Nationalized
    @Lob
    @Column(name = "password_hash")
    private String passwordHash;

    @Size(max = 50)
    @Nationalized
    @Column(name = "role", length = 50)
    private String role;

    @Size(max = 50)
    @Nationalized
    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "last_login")
    private Instant lastLogin;

    @OneToMany(mappedBy = "createdBy")
    private Set<Appointment> appointments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Patient> patients = new LinkedHashSet<>();

    @OneToOne
    private Staff staff;

}