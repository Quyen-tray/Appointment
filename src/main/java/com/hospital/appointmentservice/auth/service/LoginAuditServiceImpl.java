package com.hospital.appointmentservice.auth.service;

import com.hospital.appointmentservice.admin.service.impl.LoginAuditMapper;
import com.hospital.appointmentservice.auth.dto.LoginAuditDto;
import com.hospital.appointmentservice.auth.model.Login_audit;
import com.hospital.appointmentservice.auth.repository.Login_auditRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginAuditServiceImpl implements LoginAuditService {

    @Autowired private  Login_auditRepository repository;

    @Override
    public Page<LoginAuditDto> search(int page, int size, String sort, String username, String event, String ip,
                                      LocalDateTime from, LocalDateTime to) {
        String[] sortParts = sort.split(",");
        String sortField = sortParts[0];
        Sort.Direction direction = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Specification<Login_audit> spec = LoginAuditSpecs.filter(username, event, ip, from, to);

        return repository.findAll(spec, pageable).map(LoginAuditMapper::toDto);
    }

    @Override
    public Map<String, Object> getLoginStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("success", repository.countByEvent("LOGIN_SUCCESS"));
        stats.put("failed", repository.countByEvent("LOGIN_FAILED"));
        stats.put("first", repository.countByEvent("FIRST_LOGIN"));
        stats.put("today", repository.countTodayLogins());
        stats.put("byDay", repository.countLoginsByDay());
        return stats;
    }

    @Override
    public List<LoginAuditDto> getRecentLogins(int limit) {
        return repository.findRecentLogins(PageRequest.of(0, limit)).stream()
                .map(r -> {
                    LoginAuditDto dto = new LoginAuditDto();
                    dto.setUsername((String) r[0]);
                    dto.setTimestamp((Instant) r[1]);
                    return dto;
                })
                .toList();
    }

    @Override
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<Login_audit> logs = repository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
        ExcelExporter.exportLoginAudit(logs, response);
    }
}

