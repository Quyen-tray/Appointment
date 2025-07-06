package com.hospital.appointmentservice.patient.controller;

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

    /**
     * GET /api/labrequests/patient/{patientId}
     * Trả về danh sách lab request cho patient
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<LabRequestDto>> getLabRequestsByPatient(
            @PathVariable("patientId") UUID patientId) {
        List<LabRequestDto> dtos = labRequestService.getLabRequestsByPatientId(patientId);
        if (dtos.isEmpty()) {
            // Có thể trả 204 No Content hoặc trả 200 với list rỗng
            return ResponseEntity.ok().body(dtos);
        }
        return ResponseEntity.ok(dtos);
    }
}
