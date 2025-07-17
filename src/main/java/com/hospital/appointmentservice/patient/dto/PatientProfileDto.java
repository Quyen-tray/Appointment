package com.hospital.appointmentservice.patient.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PatientProfileDto {
    private String FullName;
    private String gender;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    private String phone;
    private String email;
    private String address;
    private String avatar;

}
