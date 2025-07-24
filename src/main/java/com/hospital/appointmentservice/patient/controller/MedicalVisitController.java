package com.hospital.appointmentservice.patient.controller;

import com.hospital.appointmentservice.patient.dto.MedicalVisitDto;
import com.hospital.appointmentservice.patient.service.MedicalVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/visits")
public class MedicalVisitController {
    private final MedicalVisitService medicalVisitService;

    @Autowired
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
    public ResponseEntity<List<MedicalVisitDto>> getVisitsByPatient( @PathVariable("patientId") UUID patientId,
                                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)     LocalDate fromDate,
                                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                                     @RequestParam(required = false, defaultValue = "0") int page,
                                                                     @RequestParam(required = false, defaultValue = "10") int size) {

        List<MedicalVisitDto> list = medicalVisitService.getVisitsByPatientId(patientId, fromDate, toDate, page, size);
        if (list == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }

    // POST /api/visits : tạo mới visit


    // PUT /api/visits/{id} : update visit


    // DELETE /api/visits/{id} : xóa visit

}
