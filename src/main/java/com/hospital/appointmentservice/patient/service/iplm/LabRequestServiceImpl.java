package com.hospital.appointmentservice.patient.service.iplm;

import com.hospital.appointmentservice.patient.dto.CreateLabRequest;
import com.hospital.appointmentservice.patient.dto.LabRequestDto;
import com.hospital.appointmentservice.patient.entity.LabRequest;
import com.hospital.appointmentservice.patient.repository.LabRequestRepository;
import com.hospital.appointmentservice.patient.repository.MedicalVisitRepository;
import com.hospital.appointmentservice.patient.service.LabRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabRequestServiceImpl implements LabRequestService {

    private final LabRequestRepository labRequestRepository;
    private final MedicalVisitRepository medicalVisitRepository;

    @Override
    public List<LabRequestDto> getLabRequestsByPatientId(UUID patientId) {
        List<LabRequest> labRequests = labRequestRepository.findByVisit_Patient_Id(patientId);
        return labRequests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public LabRequestDto createNewLabRequest(CreateLabRequest createLabRequest) {
       LabRequest labRequest = new LabRequest();
       labRequest.setRequestedBy("Doctor");
       labRequest.setStatus(createLabRequest.getStatus());
       labRequest.setPrice(createLabRequest.getPrice());
       labRequest.setTestType(createLabRequest.getTestType());
       labRequest.setVisit(medicalVisitRepository.findById(createLabRequest.getVisitId()).get());
       labRequest.setResult(createLabRequest.getResult());
       LabRequest newLab = labRequestRepository.save(labRequest);

        return convertToDto(newLab);
    }


    private LabRequestDto convertToDto(LabRequest lr) {
        return LabRequestDto.builder()
                .labId(lr.getLabId())
                .visitId(lr.getVisit() != null ? lr.getVisit().getId() : null)
                .requestedBy(lr.getRequestedBy())
                .roomId(lr.getRoomId())
                .testType(lr.getTestType())
                .result(lr.getResult())
                .status(lr.getStatus())
                .build();
    }
}
