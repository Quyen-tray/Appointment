package com.hospital.appointmentservice.patient.controller;

import com.hospital.appointmentservice.patient.dto.MedicalVisitDto;
import com.hospital.appointmentservice.patient.service.MedicalVisitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/visits")
public class MedicalVisitController {
    private final MedicalVisitService medicalVisitService;

    
    public MedicalVisitController(MedicalVisitService medicalVisitService) {
        this.medicalVisitService = medicalVisitService;
    }

    // GET /api/visits : list tất cả visits
    @GetMapping
    public ResponseEntity<List<MedicalVisitDto>> getAllVisits() {
        List<MedicalVisitDto> list = medicalVisitService.getAllVisits();
        return ResponseEntity.ok(list);
    }

    // GET /api/visits/{id} : detail 1 visit
    @GetMapping("/{id}")
    public ResponseEntity<MedicalVisitDto> getVisitById(@PathVariable("id") UUID id) {
        MedicalVisitDto dto = medicalVisitService.getVisitById(id);
        return dto != null
                ? ResponseEntity.ok(dto)
                : ResponseEntity.notFound().build();
    }

    // GET /api/visits/patient/{patientId} : list visits của 1 patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalVisitDto>> getVisitsByPatient(@PathVariable("patientId") UUID patientId) {
        List<MedicalVisitDto> list = medicalVisitService.getVisitsByPatientId(patientId);
        if (list == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }

    // POST /api/visits : tạo mới visit


    // PUT /api/visits/{id} : update visit


    // DELETE /api/visits/{id} : xóa visit

}
