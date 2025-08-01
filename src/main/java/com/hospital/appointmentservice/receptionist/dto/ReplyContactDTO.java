package com.hospital.appointmentservice.receptionist.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyContactDTO {
    private UUID contactId;
    private String subject;
    private String replyMessage;
}
