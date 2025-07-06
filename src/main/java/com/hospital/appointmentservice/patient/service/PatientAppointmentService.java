package com.hospital.appointmentservice.patient.service;

import java.util.List;
import java.util.UUID;

import com.hospital.appointmentservice.patient.dto.AppointmentRequestDto;
import com.hospital.appointmentservice.admin.model.Appointment;

public interface PatientAppointmentService {
    //lấy lịch hẹn theo patient_id
    List<Appointment> getAppointmentsByPatientId(UUID patientid);

    //lấy 1 lịch hẹn cụ thể để xử lý 
    Appointment getAppointmentById (UUID appointmentId);

    // Lưu thay đổi của lịch hẹn 
    void saveAppointment(Appointment appointment);

    // đăng kí lịch hẹn
    void createAppointment(String username , AppointmentRequestDto dto);

}
