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
            ex.printStackTrace(); // ✅ In lỗi ra log để dễ debug
            return ResponseEntity.status(500).body("Gửi liên hệ thất bại: " + ex.getMessage());
        }
    }
    @GetMapping("/all")
    public ResponseEntity<List<ContactUsDTO>> getAllContacts() {
        List<ContactUsDTO> list = contactUsService.getAllContacts();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/reply")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<String> replyToContact(@RequestBody ReplyContactDTO dto) {
        try {
            contactUsService.replyToContact(dto);
            return ResponseEntity.ok("Gửi phản hồi thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi khi gửi phản hồi: " + e.getMessage());
        }
    }

    @GetMapping("/reply-history/{contactId}")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<List<ContactReplyHistoryDTO>> getReplyHistory(@PathVariable UUID contactId) {
        return ResponseEntity.ok(contactUsService.getReplyHistoryByContactId(contactId));
    }
}
