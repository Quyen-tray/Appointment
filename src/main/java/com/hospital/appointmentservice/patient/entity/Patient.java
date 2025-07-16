package com.hospital.appointmentservice.patient.entity;

import com.hospital.appointmentservice.auth.model.UserAccount;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hospital.appointmentservice.admin.model.Appointment;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "patient_id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @Nationalized
    @Lob
    @Column(name = "full_name")
    private String fullName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dob")
    private LocalDate dob;

    @Size(max = 50)
    @Nationalized
    @Column(name = "gender", length = 50)
    private String gender;

    @Nationalized
    @Lob
    @Column(name = "phone")
    private String phone;

    @Nationalized
    @Column(name = "email")
    private String email;

    @Nationalized
    @Lob
    @Column(name = "insurance_id")
    private String insuranceId;

    @OneToMany(mappedBy = "patient")
    private Set<Appointment> appointments = new LinkedHashSet<>();


    @Column(name = "avatar")
    private String avatar ;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<MedicalVisit> medicalVisits = new LinkedHashSet<>();

    @Nationalized
    @Lob
    @Column(name = "address")
    private String address;

}