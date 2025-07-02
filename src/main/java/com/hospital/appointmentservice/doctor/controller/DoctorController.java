package com.hospital.appointmentservice.doctor.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.appointmentservice.doctor.dto.DoctorDetailDto;
import com.hospital.appointmentservice.doctor.service.DoctorService;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService ;

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorDetail(@PathVariable UUID id){
        try {
            DoctorDetailDto dto = doctorService.getDoctorDetail(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    
}
