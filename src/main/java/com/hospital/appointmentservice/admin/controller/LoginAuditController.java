package com.hospital.appointmentservice.admin.controller;

import com.hospital.appointmentservice.auth.dto.LoginAuditDto;
import com.hospital.appointmentservice.auth.service.LoginAuditService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/login-audit")
@RequiredArgsConstructor
public class LoginAuditController {

    private final LoginAuditService loginAuditService;

    @GetMapping
    public ResponseEntity<Page<LoginAuditDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp,desc") String sort,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String event,
            @RequestParam(required = false) String ip,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return ResponseEntity.ok(loginAuditService.search(page, size, sort, username, event, ip, from, to));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(loginAuditService.getLoginStats());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<LoginAuditDto>> getRecentLogins(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(loginAuditService.getRecentLogins(limit));
    }

    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        loginAuditService.exportToExcel(response);
    }
}

