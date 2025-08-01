package com.hospital.appointmentservice.patient.controller;

import com.hospital.appointmentservice.patient.dto.ContactReplyHistoryDTO;
import com.hospital.appointmentservice.patient.dto.ContactUsDTO;
import com.hospital.appointmentservice.patient.service.ContactUsService;
import com.hospital.appointmentservice.receptionist.dto.ReplyContactDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patient/contact")
public class ContactUsController {

    private final ContactUsService contactUsService;

    @Autowired
    public ContactUsController(ContactUsService contactUsService) {
        this.contactUsService = contactUsService;
    }
    @PostMapping
    public ResponseEntity<String> submitContact(@RequestBody ContactUsDTO dto) {
        try {
            contactUsService.submitContact(dto);
            return ResponseEntity.ok("Gửi liên hệ thành công!");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Gửi liên hệ thất bại: " + ex.getMessage());
        }
    }
}
