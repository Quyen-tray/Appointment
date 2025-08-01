package com.hospital.appointmentservice.receptionist.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientUpdateDTO {
    private String fullName;
    private LocalDate dob;
    private String gender;
    private String phone;
    private String email;
    private String address;
}