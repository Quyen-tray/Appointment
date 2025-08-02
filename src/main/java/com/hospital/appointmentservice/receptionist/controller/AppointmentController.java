package com.hospital.appointmentservice.receptionist.controller;


import com.hospital.appointmentservice.auth.security.JwtUtil;
import com.hospital.appointmentservice.receptionist.dto.AppointmentDTO;
import com.hospital.appointmentservice.receptionist.dto.AppointmentResponse;
import com.hospital.appointmentservice.receptionist.dto.UpdateStatusAppointmentDto;
import com.hospital.appointmentservice.receptionist.service.AppointmentService;
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
@RequestMapping("/api/appointment")
// Annotation gen constructor
@RequiredArgsConstructor
// Annotation make field private and final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentController {
    AppointmentService appointmentService;
    JwtUtil jwtUtil;

    @GetMapping("")
    public ResponseEntity<?> getAllAppointments(@RequestParam(required = false) String keyword,
                                                @RequestParam(required = false) String status,
                                                @RequestParam(required = false) Boolean isIncreaseScheduleDate) {
        try {
            List<AppointmentResponse> appointments = appointmentService.getAppointments(keyword, status, isIncreaseScheduleDate);

            return ResponseEntity.status(HttpStatus.OK).body(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errors in get appointments process" + e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointment(@PathVariable UUID id) {
        try {
            AppointmentResponse appointments = appointmentService.getAppointmentById(id);

            return ResponseEntity.status(HttpStatus.OK).body(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errors in get appointments process" + e.getMessage());
        }

    }

    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentDTO createAppointmentDTO, HttpServletRequest request) {
        try {
            String authorization = request.getHeader("Authorization");
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
            }

            String token = authorization.substring("Bearer ".length());
            Claims userClaims = jwtUtil.getAllClaims(token);
            String username = userClaims.getSubject();
            // Gọi service để tạo cuộc hẹn
            String appointment = appointmentService.createAppointment(createAppointmentDTO, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(appointment);


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errors in create appointment process: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable UUID id, @RequestBody AppointmentDTO updateAppointmentDTO, HttpServletRequest request) {
        try {
            String authorization = request.getHeader("Authorization");
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
            }

            String token = authorization.substring("Bearer ".length());
            Claims userClaims = jwtUtil.getAllClaims(token);
            String username = userClaims.getSubject();

            String appointment = appointmentService.updateAppointment(id, updateAppointmentDTO,username);
            return ResponseEntity.ok(appointment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errors in update appointment process: " + e.getMessage());
        }
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable UUID id) {
        try {
            String message = appointmentService.deleteAppointment(id);

            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errors in delete appointment process" + e.getMessage());
        }

    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable UUID id, @RequestBody UpdateStatusAppointmentDto body, HttpServletRequest request) {
        try {
            String authorization = request.getHeader("Authorization");
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
            }

            String token = authorization.substring("Bearer ".length());
            Claims userClaims = jwtUtil.getAllClaims(token);
            String username = userClaims.getSubject();
            appointmentService.updateStatusAppointment(id, body, username);
            return ResponseEntity.ok("Update status successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update status: " + e.getMessage());
        }
    }


   
    

}