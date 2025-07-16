package com.hospital.appointmentservice.auth.service;

import com.hospital.appointmentservice.auth.model.UserActivityLog;
import com.hospital.appointmentservice.auth.repository.UserActivityLogRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserActivityLogServiceImpl implements UserActivityLogService {
    @Autowired private UserActivityLogRepository userActivityLogRepository;

    @Override
    public void log(String username, String method, String endpoint, long duration, String ip) {
        UserActivityLog userActivityLog = new UserActivityLog();
        userActivityLog.setUsername(username);
        userActivityLog.setMethod(method);
        userActivityLog.setEndpoint(endpoint);
        userActivityLog.setAction(method+" "+endpoint);
        userActivityLog.setIpAddress(ip);
        userActivityLog.setDurationMs((int) duration);
        userActivityLog.setTimestamp(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        userActivityLogRepository.save(userActivityLog);
    }

    @Override
    public void logActivity(UserActivityLog log) {
        log.setTimestamp(Instant.now());
        userActivityLogRepository.save(log);
    }

    @Override
    public Page<UserActivityLog> searchLogs(String username, String action, String endpoint, Pageable pageable) {
        Specification<UserActivityLog> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (username != null && !username.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
            }
            if (action != null && !action.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("action")), "%" + action.toLowerCase() + "%"));
            }
            if (endpoint != null && !endpoint.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("endpoint")), "%" + endpoint.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return userActivityLogRepository.findAll(spec, pageable);
    }

    @Override
    public void deleteLog(Long id) {
        userActivityLogRepository.deleteById(id);
    }

    @Override
    public Optional<UserActivityLog> getLogById(Long id) {
        return userActivityLogRepository.findById(id);
    }
}
