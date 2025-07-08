package com.hospital.appointmentservice.patient.controller;

import com.hospital.appointmentservice.patient.dto.FeedBackDto;
import com.hospital.appointmentservice.patient.service.FeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/feedback")
public class FeedBackController {

    private final FeedBackService feedBackService;


    @Autowired
    public FeedBackController(FeedBackService feedBackService) {
        this.feedBackService = feedBackService;
    }

    @GetMapping
    public ResponseEntity<List<FeedBackDto>> getAllFeedBacks() {
        return ResponseEntity.ok(feedBackService.getAllFeedBack());

    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedBackDto> getFeedBackById(@PathVariable UUID id) {
        FeedBackDto dto = feedBackService.getFeedBackById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<FeedBackDto>> getFeedBackByPatient(@PathVariable UUID patientId) {
        List<FeedBackDto> list = feedBackService.getFeedBackByPatientId(patientId);
        return list == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(list);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<FeedBackDto>> getFeedBackByDoctorId(@PathVariable UUID doctorId) {
        List<FeedBackDto> list = feedBackService.getFeedBackByDoctorId(doctorId);
        return list == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody FeedBackDto dto) {
        try {
            FeedBackDto created = feedBackService.createFeedBack(dto);
            return ResponseEntity.status(201).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody FeedBackDto dto) {
        try {
            FeedBackDto updated = feedBackService.updateFeedBack(id, dto);
            return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        boolean ok = feedBackService.deleteFeedBack(id);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
