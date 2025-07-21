package com.hospital.appointmentservice.receptionist.controller;

import com.hospital.appointmentservice.receptionist.dto.AppointmentDTO;
import com.hospital.appointmentservice.admin.model.Appointment;
import com.hospital.appointmentservice.receptionist.service.impl.IAppointmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/appointment")
// Annotation gen constructor
@RequiredArgsConstructor
// Annotation make field private and final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentController {
    IAppointmentService appointmentService;

    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentDTO createAppointmentDTO) {
        try {
            Appointment appointment = appointmentService.createAppointment(createAppointmentDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errors in create appointment process" + e.getMessage());
        }

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable UUID id, @RequestBody AppointmentDTO updateAppointmentDTO) {
        try {
            Appointment appointment = appointmentService.updateAppointment(id, updateAppointmentDTO);

            return ResponseEntity.status(HttpStatus.OK).body(appointment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errors in update appointment process" + e.getMessage());
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
}