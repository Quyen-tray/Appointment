package com.hospital.appointmentservice.auth.dto;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserAccountDto {
    private String username;
    private String password;
    private String email;
    private String roles;

    public UserAccountDto() {
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

    @PostConstruct
    public void init() {
        this.roles="PATIENT";
    }


}
