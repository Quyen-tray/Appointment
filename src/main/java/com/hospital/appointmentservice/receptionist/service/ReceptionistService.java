package com.hospital.appointmentservice.receptionist.service;

import com.hospital.appointmentservice.receptionist.dto.PatientResponseDTO;
import com.hospital.appointmentservice.receptionist.dto.PatientDetailDTO;
import com.hospital.appointmentservice.receptionist.dto.PatientHistoryDTO;
import com.hospital.appointmentservice.admin.model.Appointment;
import com.hospital.appointmentservice.receptionist.entity.MedicalRecord;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.auth.model.UserAccount;
import com.hospital.appointmentservice.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class ReceptionistService {

    @Autowired
    private PatientRepository patientRepository;

    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        List<PatientResponseDTO> dtos = new ArrayList<>();

        for (Patient p : patients) {
            List<PatientHistoryDTO> history = new ArrayList<>();
            Set<Appointment> appointments = p.getAppointments();

            if (appointments != null) {
                for (Appointment a : appointments) {
                    MedicalRecord mr = a.getMedicalRecord();
                    history.add(new PatientHistoryDTO(
                            a.getAppointmentDate() != null ? a.getAppointmentDate().toString() : null,
                            a.getReason(),
                            a.getStatus(),
                            mr != null ? mr.getDiagnosis() : null,
                            mr != null ? mr.getNotes() : null,
                            (mr != null && mr.getCreatedAt() != null) ? mr.getCreatedAt().toString() : null
                    ));
                }
            }

            dtos.add(new PatientResponseDTO(
                    p.getId(),
                    p.getFullName(),
                    p.getEmail(),
                    p.getPhone(),
                    p.getDob(),
                    p.getGender(),
                    p.getAddress(),
                    history
            ));
        }

        return dtos;
    }

    public PatientDetailDTO getPatientWithHistory(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        UserAccount user = patient.getUser();

        List<PatientHistoryDTO> history = new ArrayList<>();
        Set<Appointment> appointments = patient.getAppointments();

        if (appointments != null) {
            for (Appointment a : appointments) {
                MedicalRecord mr = a.getMedicalRecord();
                history.add(new PatientHistoryDTO(
                        a.getAppointmentDate() != null ? a.getAppointmentDate().toString() : null,
                        a.getReason(),
                        a.getStatus(),
                        mr != null ? mr.getDiagnosis() : null,
                        mr != null ? mr.getNotes() : null,
                        (mr != null && mr.getCreatedAt() != null) ? mr.getCreatedAt().toString() : null
                ));
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
        if (patient != null) {
            Set<Appointment> appointments = patient.getAppointments();
            List<PatientHistoryDTO> history = new ArrayList<>();

            if (appointments != null) {
                for (Appointment a : appointments) {
                    MedicalRecord mr = a.getMedicalRecord();
                    history.add(new PatientHistoryDTO(
                            a.getAppointmentDate() != null ? a.getAppointmentDate().toString() : null,
                            a.getReason(),
                            a.getStatus(),
                            mr != null ? mr.getDiagnosis() : null,
                            mr != null ? mr.getNotes() : null,
                            (mr != null && mr.getCreatedAt() != null) ? mr.getCreatedAt().toString() : null
                    ));
                }
            }

            return new PatientResponseDTO(
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
        return null;
    }

    public Page<PatientResponseDTO> getPatientsPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Patient> patientPage = patientRepository.findAll(pageable);

        List<PatientResponseDTO> dtos = new ArrayList<>();

        for (Patient p : patientPage.getContent()) {
            List<PatientHistoryDTO> history = new ArrayList<>();
            Set<Appointment> appointments = p.getAppointments();

            if (appointments != null) {
                for (Appointment a : appointments) {
                    MedicalRecord mr = a.getMedicalRecord();
                    history.add(new PatientHistoryDTO(
                            a.getAppointmentDate() != null ? a.getAppointmentDate().toString() : null,
                            a.getReason(),
                            a.getStatus(),
                            mr != null ? mr.getDiagnosis() : null,
                            mr != null ? mr.getNotes() : null,
                            (mr != null && mr.getCreatedAt() != null) ? mr.getCreatedAt().toString() : null
                    ));
                }
            }

            dtos.add(new PatientResponseDTO(
                    p.getId(),
                    p.getFullName(),
                    p.getEmail(),
                    p.getPhone(),
                    p.getDob(),
                    p.getGender(),
                    p.getAddress(),
                    history
            ));
        }

        return new PageImpl<>(dtos, pageable, patientPage.getTotalElements());
    }
}
