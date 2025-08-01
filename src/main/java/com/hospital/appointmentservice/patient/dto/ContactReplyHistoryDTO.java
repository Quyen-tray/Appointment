package com.hospital.appointmentservice.patient.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactReplyHistoryDTO {
    private UUID id;
    private String subject;
    private String message;
    private LocalDateTime sentAt;
}
