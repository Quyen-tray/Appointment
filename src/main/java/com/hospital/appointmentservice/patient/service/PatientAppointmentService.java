package com.hospital.appointmentservice.patient.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.hospital.appointmentservice.patient.dto.AppointmentDto;
import com.hospital.appointmentservice.patient.dto.AppointmentRequestDto;
import com.hospital.appointmentservice.admin.model.Appointment;

public interface PatientAppointmentService {
    //lấy lịch hẹn theo patient_id
    List<Appointment> getAppointmentsByPatientId(UUID patientid);

    //lấy 1 lịch hẹn cụ thể để xử lý 
    Appointment getAppointmentById (UUID appointmentId);

    AppointmentDto getAppointmentDetailById(UUID appointmentId, String username);


    // Lưu thay đổi của lịch hẹn 
    void saveAppointment(Appointment appointment);

    // đăng kí lịch hẹn
    Appointment createAppointment(String username , AppointmentRequestDto dto);

    // sửa lịch hẹn ( đổi thời gian )
    void updateScheduledTime(UUID appointmentId , UUID patientId , LocalDateTime newTime );
}
