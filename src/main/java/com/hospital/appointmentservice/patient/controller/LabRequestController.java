package com.hospital.appointmentservice.patient.controller;

import com.hospital.appointmentservice.patient.dto.CreateLabRequest;
import com.hospital.appointmentservice.patient.dto.LabRequestDto;
import com.hospital.appointmentservice.patient.service.LabRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/labrequests")
@RequiredArgsConstructor
public class LabRequestController {

    private final LabRequestService labRequestService;

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<LabRequestDto>> getLabRequestsByPatient(
            @PathVariable("patientId") UUID patientId) {
        List<LabRequestDto> dtos = labRequestService.getLabRequestsByPatientId(patientId);
        if (dtos.isEmpty()) {
            return ResponseEntity.ok().body(dtos);
        }
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<?> createNewLabRequest(@RequestBody CreateLabRequest createLabRequest) {
        labRequestService.createNewLabRequest(createLabRequest);
        return ResponseEntity.ok("Create successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLabRequest(@PathVariable UUID id) {
        labRequestService.deleteLabRequest(id);
        return ResponseEntity.ok("Delete successfully");
    }
}
