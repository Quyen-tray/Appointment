package com.hospital.appointmentservice.patient.service;

import com.hospital.appointmentservice.patient.dto.PatientDto;
import com.hospital.appointmentservice.patient.dto.PatientProfileDto;
import com.hospital.appointmentservice.patient.dto.UpdateProfileRequestDto;
import com.hospital.appointmentservice.patient.entity.Patient;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    public List<PatientDto> getPatients();
    public PatientDto getPatientById(UUID id);
    public Patient addPatient(PatientDto patientDto);
    public Boolean existsPatientByEmail(String email);
    public PatientProfileDto getProfileByUserName(String username);
    void updateProfile(String username , UpdateProfileRequestDto dto);
}
