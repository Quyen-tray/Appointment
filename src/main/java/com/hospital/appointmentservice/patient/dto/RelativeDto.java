package com.hospital.appointmentservice.patient.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;

@Data
public class RelativeDto {
     private UUID id;
    private String fullName;
    private LocalDate dob;
    private String gender;
    private String relation;
    private String note;
}
