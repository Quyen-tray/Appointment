package com.hospital.appointmentservice.patient.dto;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {
    private UUID id;
    private BigDecimal amount;
    private String status;
    private LocalDateTime issuedDate;
}

