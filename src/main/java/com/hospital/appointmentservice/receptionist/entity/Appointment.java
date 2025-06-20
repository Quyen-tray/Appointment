package com.hospital.appointmentservice.receptionist.entity;

import com.hospital.appointmentservice.auth.model.UserAccount;
import com.hospital.appointmentservice.patient.entity.Patient;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
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
    private Instant scheduledTime;

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