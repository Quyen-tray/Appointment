package com.hospital.appointmentservice.doctor.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDto {
    private UUID id;
    private String name ;
}
