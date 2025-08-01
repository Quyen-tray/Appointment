package com.hospital.appointmentservice.doctor.service;

import com.hospital.appointmentservice.doctor.dto.UpdateDoctorAppointmentDto;
import com.hospital.appointmentservice.receptionist.dto.AppointmentResponse;

import java.util.List;
import java.util.UUID;

public interface DoctorAppointmentService {
    List<AppointmentResponse> getTodayAppointmentsForDoctor(String userName);
    void updateDoctorAppointment(UUID appointmentId, UpdateDoctorAppointmentDto updateData);

}
