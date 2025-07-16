package com.hospital.appointmentservice.auth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "user_activity_log")
public class UserActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100)
    @NotNull
    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Size(max = 10)
    @Column(name = "method", length = 10)
    private String method;

    @NotNull
    @Column(name = "endpoint", nullable = false)
    private String endpoint;

    @Size(max = 100)
    @Column(name = "\"action\"", length = 100)
    private String action;

    @Column(name = "duration_ms")
    private Integer durationMs;

    @NotNull
    @ColumnDefault("getdate()")
    @Column(name = "\"timestamp\"", nullable = false)
    private Instant timestamp;

    @Size(max = 50)
    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Lob
    @Column(name = "user_agent")
    private String userAgent;

}