package com.hospital.appointmentservice.patient.controller;

import com.hospital.appointmentservice.patient.dto.ContactUsDTO;
import com.hospital.appointmentservice.patient.service.ContactUsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient/contact")
public class ContactUsController {

    private final ContactUsService contactUsService;

    @Autowired
    public ContactUsController(ContactUsService contactUsService) {
        this.contactUsService = contactUsService;
    }

    /**
     * Gửi liên hệ từ người dùng không đăng nhập (trang public)
     * POST /api/patient/contact
     * Body: {
     *   "name": "Nguyễn Văn A",
     *   "email": "a@gmail.com",
     *   "subject": "Hỏi khám",
     *   "message": "Cho hỏi lịch khám cuối tuần?"
     * }
     */
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
}
