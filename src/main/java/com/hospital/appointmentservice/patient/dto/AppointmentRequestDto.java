package com.hospital.appointmentservice.patient.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDto {
    private UUID doctorId;
    private LocalDateTime scheduledTime;
    private UUID relativeId;
    private String reason;
}
