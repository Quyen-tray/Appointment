package com.hospital.appointmentservice.patient.controller;

import com.hospital.appointmentservice.patient.dto.PatientDto;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hospital.appointmentservice.patient.dto.MedicalVisitDto;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/patient")
public class PatientController {
    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        List<PatientDto> patients = patientService.getPatients();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable("id") UUID id) {
        PatientDto dto = patientService.getPatientById(id);
        return dto != null
                ? ResponseEntity.ok(dto)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/visits")
    public ResponseEntity<List<MedicalVisitDto>> getVisitsOfPatient(@PathVariable("id") UUID id) {
        List<MedicalVisitDto> visits = patientService.getMedicalVisitsByPatientId(id);
        if (visits == null) {
            return ResponseEntity.notFound().build(); // patient không tồn tại
        }
        return ResponseEntity.ok(visits);
    }



}
