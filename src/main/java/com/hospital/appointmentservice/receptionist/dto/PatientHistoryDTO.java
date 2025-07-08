package com.hospital.appointmentservice.receptionist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientHistoryDTO {
    private String appointmentDate;
    private String reason;
    private String status;
    private String diagnosis;
    private String notes;
    private String createdAt;
}

