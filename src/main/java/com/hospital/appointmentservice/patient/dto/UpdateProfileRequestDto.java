package com.hospital.appointmentservice.patient.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UpdateProfileRequestDto {
    private String name;
    private String email;
    private String phone;
    private String gender;
    private LocalDate dob;
    private String avatar;
    private String address;
}
