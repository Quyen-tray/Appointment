package com.hospital.appointmentservice.receptionist.service;

import com.hospital.appointmentservice.receptionist.dto.PatientResponseDTO;
import com.hospital.appointmentservice.receptionist.dto.PatientDetailDTO;
import com.hospital.appointmentservice.receptionist.dto.PatientHistoryDTO;
import com.hospital.appointmentservice.admin.model.Appointment;
import com.hospital.appointmentservice.receptionist.entity.MedicalRecord;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.repository.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.*;

@Service
public class ReceptionistService {

    @Autowired
    private PatientRepository patientRepository;

    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(this::mapToPatientResponseDTO).collect(Collectors.toList());
    }

    public Page<PatientResponseDTO> getPatientsPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Patient> patientPage = patientRepository.findAll(pageable);

        List<PatientResponseDTO> dtos = patientPage.getContent()
                .stream()
                .map(this::mapToPatientResponseDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, patientPage.getTotalElements());
    }

    public List<PatientResponseDTO> getPatientsByGender(String gender) {
        List<Patient> patients = patientRepository.findAllByGender(gender);
        return patients.stream().map(this::mapToPatientResponseDTO).collect(Collectors.toList());
    }

    public List<PatientResponseDTO> getPatientsByStatus(String status) {
        List<Patient> patients = patientRepository.findAll();

        List<PatientResponseDTO> dtos = new ArrayList<>();

        for (Patient p : patients) {
            Set<Appointment> appointments = p.getAppointments();
            if (appointments != null && !appointments.isEmpty()) {
                // Lấy appointment gần nhất theo scheduledTime
                Optional<Appointment> latestAppointment = appointments.stream()
                        .filter(a -> a.getScheduledTime() != null)
                        .max(Comparator.comparing(Appointment::getScheduledTime));

                if (latestAppointment.isPresent() &&
                        latestAppointment.get().getStatus() != null &&
                        latestAppointment.get().getStatus().equalsIgnoreCase(status)) {

                    dtos.add(mapToPatientResponseDTO(p));
                }
            }
        }

        return dtos;
    }

    public PatientDetailDTO getPatientWithHistory(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        List<PatientHistoryDTO> history = new ArrayList<>();
        Set<Appointment> appointments = patient.getAppointments();

        if (appointments != null) {
            for (Appointment a : appointments) {
                if (a.getMedicalRecord() != null && "Completed".equalsIgnoreCase(a.getStatus())) {
                    history.add(new PatientHistoryDTO(
                            a.getScheduledTime() != null ? a.getScheduledTime().toString() : null,
                            a.getReason(),
                            a.getStatus(),
                            a.getMedicalRecord().getDiagnosis(),
                            a.getMedicalRecord().getNotes(),
                            (a.getMedicalRecord().getCreatedAt() != null) ? a.getMedicalRecord().getCreatedAt().toString() : null
                    ));
                }
            }
        }

        return new PatientDetailDTO(
                patient.getId(),
                patient.getFullName(),
                patient.getEmail(),
                patient.getPhone(),
                patient.getDob(),
                patient.getGender(),
                patient.getAddress(),
                history
        );
    }

    public PatientResponseDTO getPatientById(UUID id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        return patient != null ? mapToPatientResponseDTO(patient) : null;
    }

    private PatientResponseDTO mapToPatientResponseDTO(Patient p) {
        List<PatientHistoryDTO> history = new ArrayList<>();
        Set<Appointment> appointments = p.getAppointments();

        String latestStatus = null;

        if (appointments != null && !appointments.isEmpty()) {
            // Sắp xếp theo scheduledTime gần nhất
            Optional<Appointment> latestAppointment = appointments.stream()
                    .filter(a -> a.getScheduledTime() != null)
                    .max(Comparator.comparing(Appointment::getScheduledTime));

            if (latestAppointment.isPresent()) {
                latestStatus = latestAppointment.get().getStatus();
            }

            for (Appointment a : appointments) {
                MedicalRecord mr = a.getMedicalRecord();
                if (mr != null && "Completed".equalsIgnoreCase(a.getStatus())) {
                    history.add(new PatientHistoryDTO(
                            a.getScheduledTime() != null ? a.getScheduledTime().toString() : null,
                            a.getReason(),
                            a.getStatus(),
                            mr.getDiagnosis(),
                            mr.getNotes(),
                            (mr.getCreatedAt() != null) ? mr.getCreatedAt().toString() : null
                    ));
                }
            }

        }

        return new PatientResponseDTO(
                p.getId(),
                p.getFullName(),
                p.getEmail(),
                p.getPhone(),
                p.getDob(),
                p.getGender(),
                p.getAddress(),
                history,
                latestStatus
        );
    }
}
