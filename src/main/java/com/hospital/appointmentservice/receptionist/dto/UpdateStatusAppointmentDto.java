package com.hospital.appointmentservice.receptionist.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusAppointmentDto {
    private String status;
    private UUID roomId;
}
