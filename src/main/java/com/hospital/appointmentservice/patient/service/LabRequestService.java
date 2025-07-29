package com.hospital.appointmentservice.patient.service;

import com.hospital.appointmentservice.patient.dto.CreateLabRequest;
import com.hospital.appointmentservice.patient.dto.LabRequestDto;

import java.util.List;
import java.util.UUID;

public interface LabRequestService {

    List<LabRequestDto> getLabRequestsByPatientId(UUID patientId);
    LabRequestDto createNewLabRequest(CreateLabRequest createLabRequest);
}