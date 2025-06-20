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
public class Login_audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100)
    @NotNull
    @Column(name = "username", nullable = false, length = 100)
    private String username;

//    LOGIN_SUCCESS, LOGOUT, LOGIN_FAILED, FIRST_LOGIN
    @Size(max = 50)
    @NotNull
    @Column(name = "event", nullable = false, length = 50)
    private String event;

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