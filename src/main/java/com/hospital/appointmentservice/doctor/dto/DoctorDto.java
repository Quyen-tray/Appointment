package com.hospital.appointmentservice.doctor.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDto {
    private UUID id;
    private String fullName;
    private String specialization;
    private String licenseNo;
}
