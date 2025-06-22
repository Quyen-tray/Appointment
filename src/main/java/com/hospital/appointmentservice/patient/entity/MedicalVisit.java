package com.hospital.appointmentservice.patient.entity;

import com.hospital.appointmentservice.receptionist.entity.Appointment;
import com.hospital.appointmentservice.admin.model.Doctor;
import com.hospital.appointmentservice.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MedicalVisit")
public class MedicalVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "visit_id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Nationalized
    @Lob
    @Column(name = "diagnosis")
    private String diagnosis;

    @Nationalized
    @Lob
    @Column(name = "note")
    private String note;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}


