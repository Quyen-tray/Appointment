package com.hospital.appointmentservice.admin.controller;

import com.hospital.appointmentservice.admin.dto.PatientAdminDto;
import com.hospital.appointmentservice.admin.service.PatientAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/patient")
public class PatientAdminController {
    @Autowired private PatientAdminService patientAdminService;
    @GetMapping("/list")
    public ResponseEntity<Page<PatientAdminDto>> listPatient(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(defaultValue = "") String keyword) {
        return ResponseEntity.ok(patientAdminService.getAll(page, size, sortBy, direction, keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientAdminDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(patientAdminService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientAdminDto> update(@PathVariable UUID id, @RequestBody PatientAdminDto dto) {
        return ResponseEntity.ok(patientAdminService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        patientAdminService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
