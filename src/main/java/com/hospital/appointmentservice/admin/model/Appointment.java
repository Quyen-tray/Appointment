package com.hospital.appointmentservice.admin.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.Nationalized;

import com.hospital.appointmentservice.auth.model.UserAccount;
import com.hospital.appointmentservice.patient.entity.Patient;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Appointment {
     @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "appointment_id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private com.hospital.appointmentservice.admin.model.Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private com.hospital.appointmentservice.admin.model.Room room;

    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;

    @Size(max = 50)
    @Nationalized
    @Column(name = "status", length = 50)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private UserAccount createdBy;

    @Size(max = 50)
    @Nationalized
    @Column(name = "created_role", length = 50)
    private String createdRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private com.hospital.appointmentservice.admin.model.Receptionist approvedBy;

    @Size(max = 50)
    @Nationalized
    @Column(name = "approval_status", length = 50)
    private String approvalStatus;

    @Column(name = "approved_at")
    private Instant approvedAt;

}
