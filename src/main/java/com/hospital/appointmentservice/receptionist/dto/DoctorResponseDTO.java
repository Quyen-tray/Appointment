package com.hospital.appointmentservice.receptionist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponseDTO {
    private UUID id;
    private String fullName;
    private String email;
    private String phone;
    private String specialty;
    private String departmentName;
    private String status;
}

