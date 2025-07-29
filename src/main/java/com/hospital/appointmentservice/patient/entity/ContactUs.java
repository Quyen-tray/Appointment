package com.hospital.appointmentservice.patient.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "contact_us")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactUs {

    @Id
    @Column(name = "id", columnDefinition = "uniqueidentifier", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID();  // ✅ Tự sinh UUID nếu chưa có
        }
        this.createdAt = LocalDateTime.now();
    }
    @Column(nullable = false)
    private boolean replied;

}
