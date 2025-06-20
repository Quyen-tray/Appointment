package com.hospital.appointmentservice.patient.service.iplm;

import com.hospital.appointmentservice.auth.service.AuthService;
import com.hospital.appointmentservice.patient.dto.PatientDto;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.repository.PatientRepository;
import com.hospital.appointmentservice.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceIplm implements PatientService {
    private final PatientRepository patientRepository;
    private final AuthService authService;

    @Autowired
    public PatientServiceIplm(PatientRepository patientRepository, AuthService authService) {
        this.patientRepository = patientRepository;
        this.authService = authService;
    }

    @Override
    public List<PatientDto> getPatients() {

        return List.of();
    }

    @Override
    public PatientDto getPatientById(UUID id) {
        return null;
    }
//This method used add patient when register account so don't used add patient if not register account
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

}
