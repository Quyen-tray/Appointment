package com.hospital.appointmentservice.patient.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotifyUnpaidDto {
    List<UUID> invoiceIds;

}
