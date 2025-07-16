package com.hospital.appointmentservice.patient.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class AppointmentBookingResponeDto {
    private String message ;
    private UUID appointmentId ;
}
