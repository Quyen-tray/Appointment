package com.hospital.appointmentservice.patient.dto;

import lombok.Data;

@Data
public class TransactionResponse {
    private int price;
    private String data, message;
}
