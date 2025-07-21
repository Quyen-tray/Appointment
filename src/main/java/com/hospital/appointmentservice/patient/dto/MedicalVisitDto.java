package com.hospital.appointmentservice.patient.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalVisitDto {
    private String PatientId;
    private String id;
    private String appointmentId;
    private String doctorId;
    private String diagnosis;
    private String note;
    private String createdAt;
    private String doctorName;

}
