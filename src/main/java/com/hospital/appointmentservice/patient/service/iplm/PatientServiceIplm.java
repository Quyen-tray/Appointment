package com.hospital.appointmentservice.patient.service.iplm;

import com.hospital.appointmentservice.auth.service.AuthService;
import com.hospital.appointmentservice.patient.dto.PatientDto;
import com.hospital.appointmentservice.patient.dto.MedicalVisitDto;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.entity.MedicalVisit;
import com.hospital.appointmentservice.patient.repository.PatientRepository;
import com.hospital.appointmentservice.patient.repository.MedicalVisitRepository;
import com.hospital.appointmentservice.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceIplm implements PatientService {
    private final PatientRepository patientRepository;
    private final MedicalVisitRepository medicalVisitRepository;
    private final AuthService authService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Autowired
    public PatientServiceIplm(PatientRepository patientRepository,
                              MedicalVisitRepository medicalVisitRepository,
                              AuthService authService) {
        this.patientRepository = patientRepository;
        this.medicalVisitRepository = medicalVisitRepository;
        this.authService = authService;
    }

    @Override
    public List<PatientDto> getPatients() {
        return patientRepository.findAll().stream()
                .map(patient -> {
                    PatientDto dto = new PatientDto();
                    dto.setId(patient.getId().toString());
                    dto.setUserId(patient.getUser() != null
                            ? patient.getUser().getId().toString()
                            : null);
                    dto.setFullName(patient.getFullName());
                    dto.setDob(patient.getDob() != null ? patient.getDob().toString() : null);
                    dto.setGender(patient.getGender());
                    dto.setPhone(patient.getPhone());
                    dto.setEmail(patient.getEmail());
                    dto.setInsuranceId(patient.getInsuranceId());
                    dto.setMedicalVisits(Collections.emptyList());
                    return dto;
                }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PatientDto getPatientById(UUID id) {
        return patientRepository.findWithVisitsById(id)
                .map(patient -> {
                    PatientDto dto = new PatientDto();
                    dto.setId(patient.getId().toString());
                    dto.setUserId(patient.getUser() != null ? patient.getUser().getId().toString() : null);
                    dto.setFullName(patient.getFullName());
                    dto.setDob(patient.getDob() != null ? patient.getDob().toString() : null);
                    dto.setGender(patient.getGender());
                    dto.setPhone(patient.getPhone());
                    dto.setEmail(patient.getEmail());
                    dto.setInsuranceId(patient.getInsuranceId());

                    // Map medicalVisits
                    List<MedicalVisitDto> visits = new ArrayList<>();
                    if (patient.getMedicalVisits() != null) {
                        for (MedicalVisit mv : patient.getMedicalVisits()) {
                            MedicalVisitDto mvDto = new MedicalVisitDto();
                            mvDto.setId(mv.getId().toString());
                            if (mv.getAppointment() != null && mv.getAppointment().getId() != null) {
                                mvDto.setAppointmentId(mv.getAppointment().getId().toString());
                            }
                            if (mv.getDoctor() != null && mv.getDoctor().getId() != null) {
                                mvDto.setDoctorId(mv.getDoctor().getId().toString());
                            }
                            mvDto.setDiagnosis(mv.getDiagnosis());
                            mvDto.setNote(mv.getNote());
                            if (mv.getCreatedAt() != null) {
                                mvDto.setCreatedAt(mv.getCreatedAt().format(dateFormatter));
                            }
                            visits.add(mvDto);
                        }
                    }
                    dto.setMedicalVisits(visits);
                    return dto;
                })
                .orElse(null);
    }

    @Override
    public Patient addPatient(PatientDto patientDto) {
        Patient patient = new Patient();
        patient.setUser(authService.findByUserAccount(patientDto.getFullName()));
        patient.setEmail(patientDto.getEmail());
        return patientRepository.save(patient);
    }

    @Override
    public Boolean existsPatientByEmail(String email) {
        return patientRepository.existsPatientByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalVisitDto> getMedicalVisitsByPatientId(UUID patientId) {
        if (!patientRepository.existsById(patientId)) {
            return null;
        }
        List<MedicalVisit> visits = medicalVisitRepository.findByPatientId(patientId);
        List<MedicalVisitDto> dtos = new ArrayList<>();
        for (MedicalVisit mv : visits) {
            MedicalVisitDto mvDto = new MedicalVisitDto();
            mvDto.setId(mv.getId().toString());
            if (mv.getAppointment() != null && mv.getAppointment().getId() != null) {
                mvDto.setAppointmentId(mv.getAppointment().getId().toString());
            }
            if (mv.getDoctor() != null && mv.getDoctor().getId() != null) {
                mvDto.setDoctorId(mv.getDoctor().getId().toString());
            }
            mvDto.setDiagnosis(mv.getDiagnosis());
            mvDto.setNote(mv.getNote());
            if (mv.getCreatedAt() != null) {
                mvDto.setCreatedAt(mv.getCreatedAt().format(dateFormatter));
            }
            dtos.add(mvDto);
        }
        return dtos;
    }
}
