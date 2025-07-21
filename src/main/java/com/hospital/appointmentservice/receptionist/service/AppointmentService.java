package com.hospital.appointmentservice.receptionist.service;

import com.hospital.appointmentservice.admin.repository.DoctorRepository;
import com.hospital.appointmentservice.admin.repository.ReceptionistRoomRepository;
import com.hospital.appointmentservice.patient.repository.AppointmentRepository;
import com.hospital.appointmentservice.patient.repository.PatientRepository;
import com.hospital.appointmentservice.receptionist.dto.AppointmentDTO;
import com.hospital.appointmentservice.admin.model.Appointment;
import com.hospital.appointmentservice.receptionist.service.impl.IAppointmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentService implements IAppointmentService {
    AppointmentRepository appointmentRepository;
    PatientRepository patientRepository;
    DoctorRepository doctorRepository;
    ReceptionistRoomRepository roomRepository;

    @Override
    public Appointment createAppointment(AppointmentDTO createAppointmentDTO) {
        Appointment appointment = new Appointment();
        return auditAppointment(createAppointmentDTO, appointment);
    }

    @Override
    public Appointment updateAppointment(UUID id, AppointmentDTO updateAppointmentDTO) {
        // Find appointment by id
        // If not found, throw exception
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with id: " + id));
        return auditAppointment(updateAppointmentDTO, appointment);
    }

    @Override
    public String deleteAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with id: " + id));
        appointmentRepository.delete(appointment);
        return "Delete success";
    }

    private Appointment auditAppointment(AppointmentDTO appointmentDTO, Appointment appointment) {
        appointment.setPatient(patientRepository.findById(appointmentDTO.getPatientId()).orElse(null));
        appointment.setDoctor(doctorRepository.findById(appointmentDTO.getDoctorId()).orElse(null));
        appointment.setRoom(roomRepository.findById(appointmentDTO.getRoomId()).orElse(null));
        appointment.setScheduledTime(appointmentDTO.getScheduledTime());
        appointment.setStatus(appointmentDTO.getStatus());
        appointment.setCreatedBy(null);
        appointment.setCreatedRole(appointmentDTO.getCreatedRole());
        appointment.setApprovedBy(null);
        appointment.setApprovalStatus(appointmentDTO.getApprovalStatus());
        appointment.setApprovedAt(appointmentDTO.getApprovedAt());

        return appointmentRepository.save(appointment);
    }


}
