package com.hospital.appointmentservice.patient.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.appointmentservice.patient.dto.RelativeDto;
import com.hospital.appointmentservice.patient.dto.RelativeResponseDto;
import com.hospital.appointmentservice.patient.service.RelativeService;

@RestController
@RequestMapping("/api/patient/relatives")
public class RelativeController {

    @Autowired
    private RelativeService relativeService;

    @GetMapping("/all")
    public ResponseEntity<List<RelativeDto>> getRelatives(Principal principal) {
        String username = principal.getName();
        List<RelativeDto> relatives = relativeService.getRelativesByUsername(username);
        return ResponseEntity.ok(relatives);
    }

    @PostMapping("/add")
    public ResponseEntity<RelativeDto> addRelative(Principal principal, @RequestBody RelativeDto dto) {
        String username = principal.getName();
        RelativeDto saved = relativeService.addRelative(username, dto);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RelativeDto> updateRelative(@PathVariable UUID id, @RequestBody RelativeDto dto,
            Principal principal) {
        String username = principal.getName();
        RelativeDto updated = relativeService.updateRelative(id, dto, username);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRelative(@PathVariable UUID id, Principal principal) {
        String username = principal.getName();
        relativeService.deleteRelative(id, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list_relative")
    public ResponseEntity<List<RelativeResponseDto>> getSummary(Principal principal) {
    String username = principal.getName();
    List<RelativeResponseDto> list = relativeService.getRelativesSummaryByUsername(username);
    return ResponseEntity.ok(list);
}

}
