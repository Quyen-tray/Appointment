package com.hospital.appointmentservice.admin.controller;

import com.hospital.appointmentservice.admin.dto.PatientAdminDto;
import com.hospital.appointmentservice.admin.dto.ReceptionistAdminDto;
import com.hospital.appointmentservice.admin.service.ReceptionistAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/receptionist")
public class ReceptionistAdminController {
    @Autowired
    private ReceptionistAdminService service;

    @GetMapping("/list")
    public ResponseEntity<Page<ReceptionistAdminDto>> listPatient(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(defaultValue = "") String keyword) {
        return ResponseEntity.ok(service.getAll(page, size, sortBy, direction, keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceptionistAdminDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceptionistAdminDto> update(@PathVariable UUID id, @RequestBody ReceptionistAdminDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
