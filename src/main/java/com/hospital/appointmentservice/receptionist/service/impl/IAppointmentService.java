package com.hospital.appointmentservice.receptionist.service.impl;

import com.hospital.appointmentservice.receptionist.dto.AppointmentDTO;
import com.hospital.appointmentservice.receptionist.dto.AppointmentResponse;
import com.hospital.appointmentservice.receptionist.dto.UpdateStatusAppointmentDto;
import java.util.List;
import java.util.UUID;

public interface IAppointmentService {
    List<AppointmentResponse> getAppointments(String keyword, String status, Boolean isIncreaseScheduleDate);
    AppointmentResponse getAppointmentById(UUID id);
    String createAppointment(AppointmentDTO createAppointmentDTO, String username);
    String updateAppointment(UUID id, AppointmentDTO updateAppointmentDTO, String username);
    String deleteAppointment(UUID id);
    void updateStatusAppointment(UUID id, UpdateStatusAppointmentDto body, String username);
//    Appointment changeStatus(UUID id, String status);
}
