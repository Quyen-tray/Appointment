package com.hospital.appointmentservice.receptionist.service.impl;

import com.hospital.appointmentservice.receptionist.dto.AppointmentDTO;
import com.hospital.appointmentservice.receptionist.entity.Appointment;

import java.util.UUID;

public interface IAppointmentService {
    Appointment createAppointment(AppointmentDTO createAppointmentDTO);
    Appointment updateAppointment(UUID id, AppointmentDTO updateAppointmentDTO);
    String deleteAppointment(UUID id);
}
