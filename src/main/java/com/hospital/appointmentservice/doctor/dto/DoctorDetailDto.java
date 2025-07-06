package com.hospital.appointmentservice.doctor.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDetailDto {
    private UUID id ;
    private String fullName;
    private String specialty ;
    private String description ;
    private String positionTitle ;
}
