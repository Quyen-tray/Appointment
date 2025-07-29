package com.hospital.appointmentservice.patient.service;

import com.hospital.appointmentservice.patient.dto.MedicalVisitDto;
import com.hospital.appointmentservice.patient.dto.MedicalVisitResponse;
import com.hospital.appointmentservice.patient.dto.UpdateMedicalNote;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MedicalVisitService {
    List<MedicalVisitDto> getAllVisits();
    MedicalVisitDto getVisitById(UUID id);
    List<MedicalVisitDto> getVisitsByPatientId(UUID patientId,LocalDate fromDate, LocalDate toDate, int page, int size);
    MedicalVisitResponse getByAppointmentAndPatientId(UUID appointmentId, UUID patientId);
    MedicalVisitDto createVisit(MedicalVisitDto dto);
    MedicalVisitDto updateVisit(UUID id, MedicalVisitDto dto);
    void deleteVisit(UUID id);
    void updateNote(UUID id, UpdateMedicalNote update);
}
