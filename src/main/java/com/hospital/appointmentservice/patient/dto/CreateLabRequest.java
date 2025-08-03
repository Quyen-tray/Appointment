package com.hospital.appointmentservice.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class CreateLabRequest {
    private UUID visitId;
    private UUID roomId;
    private String testType;
    private String result;
    private String status;
    private BigDecimal price;
}
