package com.hospital.appointmentservice.patient.service;

import com.hospital.appointmentservice.patient.dto.LabRequestDto;

import java.util.List;
import java.util.UUID;

public interface LabRequestService {
    /**
     * Lấy danh sách LabRequest theo patientId
     */
    List<LabRequestDto> getLabRequestsByPatientId(UUID patientId);
}
