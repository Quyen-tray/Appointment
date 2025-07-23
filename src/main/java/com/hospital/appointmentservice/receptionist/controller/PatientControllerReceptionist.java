package com.hospital.appointmentservice.receptionist.controller;

import com.hospital.appointmentservice.receptionist.dto.PatientDetailDTO;
import com.hospital.appointmentservice.receptionist.dto.PatientResponseDTO;
import com.hospital.appointmentservice.receptionist.service.ReceptionistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class PatientControllerReceptionist {

    @Autowired
    private ReceptionistService patientService;

    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/patients/{id}/history")
    public ResponseEntity<PatientDetailDTO> getPatientHistory(@PathVariable UUID id) {
        return ResponseEntity.ok(patientService.getPatientWithHistory(id));
    }


    @GetMapping("/patients/{id}")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable UUID id) {
        PatientResponseDTO patient = patientService.getPatientById(id);
        if (patient != null) {
            return ResponseEntity.ok(patient);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/patients/paged")
    public ResponseEntity<Page<PatientResponseDTO>> getPatientsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(patientService.getPatientsPaged(page, size));
    }
    @GetMapping("/patients/filter")
    public ResponseEntity<List<PatientResponseDTO>> getPatientsByGender(@RequestParam String gender) {
        return ResponseEntity.ok(patientService.getPatientsByGender(gender));
    }
    @GetMapping("/patients/status")
    public ResponseEntity<List<PatientResponseDTO>> getPatientsByStatus(@RequestParam String status) {
        return ResponseEntity.ok(patientService.getPatientsByStatus(status));
    }

}
