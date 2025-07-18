package com.hospital.appointmentservice.doctor.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorListDto {
    private UUID id ;
    private String fullName ;
}