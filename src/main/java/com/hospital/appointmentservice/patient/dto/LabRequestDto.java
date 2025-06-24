package com.hospital.appointmentservice.patient.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabRequestDto {
    private UUID labId;
    private UUID visitId;
    private String requestedBy;
    private UUID roomId;
    private String testType;
    private String result;
    private String status;
    // Nếu cần thêm thông tin khác (ví dụ patientId, patientName...), có thể bổ sung
}
