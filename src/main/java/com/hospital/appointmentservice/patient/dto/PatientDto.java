package com.hospital.appointmentservice.patient.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDto {
    private String id;
    private String userId;
    private String fullName;
    private String dob;
    private String gender;
    private String phone;
    private String email;
    private String insuranceId;
}
