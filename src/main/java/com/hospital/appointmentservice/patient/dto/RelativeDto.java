package com.hospital.appointmentservice.patient.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelativeDto {
    private UUID id;
    private String fullName;
    private LocalDate dob;
    private String gender;
    private String relation;
    private String note;
}
