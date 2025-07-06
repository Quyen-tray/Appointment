package com.hospital.appointmentservice.patient.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Getter
@Setter
@ToString
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

    private List<MedicalVisitDto> medicalVisits;
}
