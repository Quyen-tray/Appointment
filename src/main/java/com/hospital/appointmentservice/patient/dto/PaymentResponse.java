package com.hospital.appointmentservice.patient.dto;

import lombok.Data;

@Data
public class PaymentResponse {
    private String status;
    private String message;
    private String URL;
}
