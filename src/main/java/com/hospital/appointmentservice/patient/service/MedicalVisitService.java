package com.hospital.appointmentservice.patient.service;

import com.hospital.appointmentservice.patient.dto.MedicalVisitDto;

import java.util.List;
import java.util.UUID;

public interface MedicalVisitService {
    List<MedicalVisitDto> getAllVisits();
    MedicalVisitDto getVisitById(UUID id);
    List<MedicalVisitDto> getVisitsByPatientId(UUID patientId);
    MedicalVisitDto createVisit(MedicalVisitDto dto);
    MedicalVisitDto updateVisit(UUID id, MedicalVisitDto dto);
    void deleteVisit(UUID id);
}
