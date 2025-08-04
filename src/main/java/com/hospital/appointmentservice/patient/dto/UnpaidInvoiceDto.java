package com.hospital.appointmentservice.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnpaidInvoiceDto {
    private UUID id;
    private BigDecimal amount;
    private String status;
    private LocalDateTime issuedDate;
    private String patientName;
    private String email;
    private String phone;
}
