package com.hospital.appointmentservice.patient.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalVisitResponse {
    private MedicalVisitDto medicalVisit;

    private List<LabRequestDto> labRequests;
}
