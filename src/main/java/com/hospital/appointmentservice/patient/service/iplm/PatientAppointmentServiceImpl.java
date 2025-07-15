package com.hospital.appointmentservice.patient.service.iplm;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.appointmentservice.admin.model.Doctor;
import com.hospital.appointmentservice.doctor.repository.DoctorRepository;
import com.hospital.appointmentservice.patient.dto.AppointmentDto;
import com.hospital.appointmentservice.patient.dto.AppointmentRequestDto;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.repository.AppointmentRepository;
import com.hospital.appointmentservice.patient.repository.PatientRepository;
import com.hospital.appointmentservice.patient.service.PatientAppointmentService;
import com.hospital.appointmentservice.admin.model.Appointment;

@Service
public class PatientAppointmentServiceImpl implements PatientAppointmentService{
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public List<Appointment> getAppointmentsByPatientId(UUID patientid) {
        return appointmentRepository.findByPatient_Id(patientid);
    }

    @Override
    public Appointment getAppointmentById(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId).orElse(null);
    }

    @Override
    public void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public Appointment createAppointment(String username, AppointmentRequestDto dto) {
        Patient patient = patientRepository.findByUser_Username(username);
        if (patient == null) {
            throw new RuntimeException("Paitent not found!");
        }

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found!"));

        boolean exists = appointmentRepository.existsByDoctorAndScheduledTime(doctor, dto.getScheduledTime());
        if (exists) {
            throw new RuntimeException("Doctor is already booked at this time!");
        }


        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setScheduledTime(dto.getScheduledTime());
        appointment.setStatus("Pending");

       appointmentRepository.save(appointment);
        return appointment;
    }

    @Override 
    public AppointmentDto getAppointmentDetailById(UUID appointmentId , String username ){
        Patient patient = patientRepository.findByUser_Username(username);
        if(patient == null ){
            throw new RuntimeException("Không tìm thấy bệnh nhân!");
        }
        Appointment appointment = getAppointmentById(appointmentId);

        if(appointment == null){
            throw new RuntimeException("Không tìm thấy lịch hẹn!");
        }

        if(!appointment.getPatient().getId().equals(patient.getId())){
            throw new RuntimeException("Bạn không có quyền xem lịch hẹn này!");
        }

        AppointmentDto dto = new AppointmentDto();
        dto.setId(appointment.getId());
        dto.setPatientName(patient.getFullName());
        dto.setDoctorName(appointment.getDoctor().getFullName());
        dto.setRoomName(appointment.getRoom() != null ? appointment.getRoom().getName() : "Chưa có phòng!");
        dto.setScheduledTime(appointment.getScheduledTime().toString());
        dto.setStatus(appointment.getStatus()); 
        return dto ;
    }

    @Override 
    public void updateScheduledTime(UUID appointmentId , UUID patientId ,  LocalDateTime newTime) {
    
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(()-> new RuntimeException("Không tìm thấy lịch hẹn!"));
        if(!appointment.getPatient().getId().equals(patientId)){
            throw new RuntimeException("Không có quyền sửa lịch hẹn này!");
        }

        if(newTime == null || newTime.isBefore(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")))){
            throw new RuntimeException("Thời gian không hợp lệ");
        }

        appointment.setScheduledTime(newTime);
        appointmentRepository.save(appointment);
    }
}
