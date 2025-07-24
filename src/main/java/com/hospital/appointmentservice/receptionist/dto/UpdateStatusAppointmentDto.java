package com.hospital.appointmentservice.receptionist.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusAppointmentDto {
    private String status;
}
