package com.hospital.appointmentservice.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "staff_id", nullable = false)
    private UUID id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "staff_id", nullable = false)
    private com.hospital.appointmentservice.admin.model.Staff staff;

    @Nationalized
    @Lob
    @Column(name = "specialization")
    private String specialization;

    @Nationalized
    @Lob
    @Column(name = "license_no")
    private String licenseNo;

    @OneToMany(mappedBy = "doctor")
    @JsonIgnore
    private Set<Appointment> appointments = new LinkedHashSet<>();

     public String getFullName() {
        return staff != null ? staff.getFullName() : null;
    }

}