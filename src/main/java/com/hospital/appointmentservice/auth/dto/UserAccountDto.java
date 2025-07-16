package com.hospital.appointmentservice.auth.dto;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

import java.util.UUID;

@Data
@Getter
@Setter
public class UserAccountDto {
    private UUID id;
    private String username;
    private String password;
    private String email;
    private String roles;
    private String status;
    private Instant lastLogin;

    public UserAccountDto() {
    }

    public UserAccountDto(UUID id, String username) {
        this.id = id;
        this.username = username;
    }

    public UserAccountDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserAccountDto(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserAccountDto(String username, String password, String email, String roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public UserAccountDto(UUID id, String username, String roles, String status, Instant lastLogin) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.status = status;
        this.lastLogin = lastLogin;
    }

    @PostConstruct
    public void init() {
        this.roles="PATIENT";
    }


}
