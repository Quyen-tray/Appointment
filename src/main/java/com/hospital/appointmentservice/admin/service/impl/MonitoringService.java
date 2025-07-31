package com.hospital.appointmentservice.admin.service.impl;
import com.hospital.appointmentservice.auth.repository.Login_auditRepository;
import com.hospital.appointmentservice.auth.repository.UserActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MonitoringService {

    private final Login_auditRepository loginAuditRepository;
    private final UserActivityLogRepository userActivityLogRepository;

    public long countLoginSuccessToday() {
        return loginAuditRepository.countLoginSuccessSince(Instant.now().truncatedTo(ChronoUnit.DAYS));
    }

    public long countLoginSuccessThisWeek() {
        Instant startOfWeek = Instant.now().minus(7, ChronoUnit.DAYS);
        return loginAuditRepository.countLoginSuccessSince(startOfWeek);
    }

    public long countLoginSuccessThisMonth() {
        Instant startOfMonth = Instant.now().minus(30, ChronoUnit.DAYS);
        return loginAuditRepository.countLoginSuccessSince(startOfMonth);
    }

    public long countLoginFailed() {
        return loginAuditRepository.countLoginFailedSince(Instant.EPOCH);
    }

    public long countFirstLogins() {
        return loginAuditRepository.countFirstLogins();
    }

    public List<Map<String, Object>> getRecentLogins(int limit) {
        return loginAuditRepository.findRecentLogins(PageRequest.of(0, limit)).stream()
                .map(r -> Map.of(
                        "username", r[0],
                        "timestamp", r[1]
                ))
                .toList();
    }

    public List<Map<String, Object>> getTopEndpoints(int limit) {
        return userActivityLogRepository.findTopEndpoints(PageRequest.of(0, limit)).stream()
                .map(obj -> Map.of("endpoint", obj[0], "count", obj[1]))
                .toList();
    }

    public List<Map<String, Object>> getApiCallsByDay() {
        return userActivityLogRepository.countApiCallsByDay().stream()
                .map(obj -> Map.of("date", obj[0], "count", obj[1]))
                .toList();
    }

    public List<Map<String, Object>> getAverageDurationPerEndpoint() {
        return userActivityLogRepository.averageDurationPerEndpoint().stream()
                .map(obj -> Map.of("endpoint", obj[0], "averageDuration", obj[1]))
                .toList();
    }

    public List<Map<String, Object>> getRequestCountByMethod() {
        return userActivityLogRepository.countRequestsByMethod().stream()
                .map(obj -> Map.of("method", obj[0], "count", obj[1]))
                .toList();
    }
}
