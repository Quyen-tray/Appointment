package com.hospital.appointmentservice.receptionist.controller;

import com.hospital.appointmentservice.receptionist.dto.DoctorResponseDTO;
import com.hospital.appointmentservice.receptionist.service.ReceptionistDoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReceptionistDoctorController {

    private final ReceptionistDoctorService doctorService;

    @Autowired
    public ReceptionistDoctorController(ReceptionistDoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }
}
