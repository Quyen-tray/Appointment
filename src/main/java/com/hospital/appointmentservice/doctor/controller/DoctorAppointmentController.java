package com.hospital.appointmentservice.doctor.controller;

import com.hospital.appointmentservice.auth.security.JwtUtil;
import com.hospital.appointmentservice.doctor.dto.UpdateDoctorAppointmentDto;
import com.hospital.appointmentservice.doctor.service.DoctorAppointmentService;
import com.hospital.appointmentservice.receptionist.dto.AppointmentResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctor/appointment")
// Annotation gen constructor
@RequiredArgsConstructor
// Annotation make field private and final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DoctorAppointmentController {
    JwtUtil jwtUtil;
    DoctorAppointmentService doctorAppointmentService;

    @GetMapping
    public ResponseEntity<?> getTodayAppointment(HttpServletRequest request) {
        try {
            String authorization = request.getHeader("Authorization");
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
            }

            String token = authorization.substring("Bearer ".length());
            Claims userClaims = jwtUtil.getAllClaims(token);
            String username = userClaims.getSubject();
            List<AppointmentResponse> blogs = doctorAppointmentService.getTodayAppointmentsForDoctor(username);
            return ResponseEntity.status(HttpStatus.OK).body(blogs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errors in get appointments process" + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctorAppointment(@PathVariable UUID id, @RequestBody UpdateDoctorAppointmentDto updateData) {
        try {
            doctorAppointmentService.updateDoctorAppointment(id,updateData);
            return ResponseEntity.status(HttpStatus.OK).body("Update successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errors in get appointments process" + e.getMessage());
        }
    }
}
