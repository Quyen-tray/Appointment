package com.hospital.appointmentservice.patient.service.iplm;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.appointmentservice.admin.model.Doctor;
import com.hospital.appointmentservice.doctor.repository.DoctorRepository;
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
    public void createAppointment(String username, AppointmentRequestDto dto) {
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

    }
}
