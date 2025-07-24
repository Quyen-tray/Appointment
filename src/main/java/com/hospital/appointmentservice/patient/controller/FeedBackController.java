package com.hospital.appointmentservice.patient.controller;

import com.hospital.appointmentservice.patient.dto.FeedBackDto;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.repository.PatientRepository;
import com.hospital.appointmentservice.patient.service.FeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/feedback")
public class FeedBackController {

    private final FeedBackService feedBackService;
    private final PatientRepository patientRepository;

    @Autowired
    public FeedBackController(FeedBackService feedBackService,
                              PatientRepository patientRepository) {
        this.feedBackService = feedBackService;
        this.patientRepository = patientRepository;
    }

    /**
     * Chỉ lấy feedback của bệnh nhân đang đăng nhập, kèm phân trang.
     */
    @GetMapping
    public ResponseEntity<List<FeedBackDto>> getMyFeedBacks(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        // Lấy patient từ username trong Principal
        Patient patient = patientRepository.findByUser_Username(principal.getName());
        if (patient == null) {
            return ResponseEntity.status(404).build();
        }

        // Lấy toàn bộ feedback của patient
        List<FeedBackDto> all = feedBackService.getFeedBackByPatientId(patient.getId());
        if (all == null || all.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        all.sort(Comparator.comparing(FeedBackDto::getCreated).reversed());

        // Phân trang thủ công
        int fromIndex = page * size;
        if (fromIndex >= all.size()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        int toIndex = Math.min(fromIndex + size, all.size());
        List<FeedBackDto> pageItems = all.subList(fromIndex, toIndex);

        return ResponseEntity.ok(pageItems);
    }

    /**
     * Tạo feedback (bệnh nhân chỉ có thể tạo cho chính họ).
     */
    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody FeedBackDto dto,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).body("Chưa đăng nhập!");
        }
        // Gán tự động patientId từ principal
        Patient patient = patientRepository.findByUser_Username(principal.getName());
        if (patient == null) {
            return ResponseEntity.status(404).body("Không tìm thấy bệnh nhân!");
        }
        dto.setPatientId(patient.getId().toString());

        try {
            FeedBackDto created = feedBackService.createFeedBack(dto);
            return ResponseEntity.status(201).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Cập nhật feedback: chỉ cập nhật nếu là của chính họ.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody FeedBackDto dto,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).body("Chưa đăng nhập!");
        }

        Patient patient = patientRepository.findByUser_Username(principal.getName());
        if (patient == null) {
            return ResponseEntity.status(404).body("Không tìm thấy bệnh nhân!");
        }

        FeedBackDto existing = feedBackService.getFeedBackById(id);
        if (existing == null || !existing.getPatientId().equals(patient.getId().toString())) {
            return ResponseEntity.status(403).body("Bạn không có quyền chỉnh sửa phản hồi này.");
        }

        // ✅ Chỉ cho sửa nếu còn trong ngày
        boolean isSameDay = LocalDate.now().equals(
                existing.getCreated().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
        );
        if (!isSameDay) {
            return ResponseEntity.status(403).body("Chỉ được chỉnh sửa phản hồi trong ngày tạo.");
        }

        try {
            FeedBackDto updated = feedBackService.updateFeedBack(id, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Xóa feedback: chỉ xóa nếu là của chính họ.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        Patient patient = patientRepository.findByUser_Username(principal.getName());
        if (patient == null) {
            return ResponseEntity.status(404).build();
        }

        FeedBackDto existing = feedBackService.getFeedBackById(id);
        if (existing == null || !existing.getPatientId().equals(patient.getId().toString())) {
            return ResponseEntity.status(403).build();
        }

        // ✅ Chỉ cho xóa nếu còn trong ngày
        boolean isSameDay = LocalDate.now().equals(
                existing.getCreated().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
        );
        if (!isSameDay) {
            return ResponseEntity.status(403).build();
        }

        boolean ok = feedBackService.deleteFeedBack(id);
        return ok
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }}
