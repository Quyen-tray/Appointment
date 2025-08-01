package com.hospital.appointmentservice.receptionist.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDetailDTO {
    private UUID id;
    private String fullName;
    private String email;
    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    private String gender;
    private String address;
}

