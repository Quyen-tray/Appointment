package com.hospital.appointmentservice.admin.controller;

import com.hospital.appointmentservice.auth.model.UserActivityLog;
import com.hospital.appointmentservice.auth.service.UserActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/log")
public class LogAdminController {
    @Autowired private UserActivityLogService userActivityLogService;
    @GetMapping
    public ResponseEntity<Map<String, Object>> search(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String endpoint,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserActivityLog> logPage = userActivityLogService.searchLogs(username, action, endpoint, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", logPage.getContent());
        response.put("totalPages", logPage.getTotalPages());
        response.put("totalElements", logPage.getTotalElements());
        response.put("page", logPage.getNumber());
        response.put("size", logPage.getSize());

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userActivityLogService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserActivityLog> getById(@PathVariable Long id) {
        return userActivityLogService.getLogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
