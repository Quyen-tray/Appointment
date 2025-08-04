package com.hospital.appointmentservice.patient.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {
      private UUID id;
    private String patientName ;
    private String status;
    private String scheduledTime;
    private String doctorName;
    private String roomName;
    private RelativeDto relative;

}
