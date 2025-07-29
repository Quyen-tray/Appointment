package com.hospital.appointmentservice.patient.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ReplyHistory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactReplyHistory {

    @Id
    @GeneratedValue
    private UUID id;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "replied_at")
    private LocalDateTime sentAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", nullable = false)
    private ContactUs contact;
}
