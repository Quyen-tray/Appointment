package com.hospital.appointmentservice.receptionist.service;

import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.receptionist.dto.PatientDetailDTO;
import com.hospital.appointmentservice.receptionist.dto.PatientHistoryDTO;
import com.hospital.appointmentservice.receptionist.dto.PatientRegisterRequest;

import java.util.List;
import java.util.UUID;

public interface ReceptionistPatientService {
    List<PatientDetailDTO> getAllPatients();
    List<PatientHistoryDTO> getPatientHistory(UUID patientId);
    Patient getPatientById(UUID id);
    Patient savePatient(Patient patient);
    void addNewPatient(PatientRegisterRequest request);

}
