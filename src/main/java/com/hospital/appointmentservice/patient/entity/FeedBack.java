package com.hospital.appointmentservice.patient.entity;


import com.hospital.appointmentservice.admin.model.Doctor; // hoặc đúng package nếu khác
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FeedBack")
public class FeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "feedback_id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Min(1)
    @Max(10)
    @Column(name = "score", nullable = false)
    private Integer score;

    @Nationalized
    @Lob
    @Column(name = "comment")
    private String comment;

    @Column(name = "date_create", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreate;

    @PrePersist
    protected void onCreate() {
        this.dateCreate = new Date();
    }
}
