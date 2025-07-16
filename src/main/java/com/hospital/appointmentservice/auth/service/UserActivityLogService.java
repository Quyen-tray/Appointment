package com.hospital.appointmentservice.auth.service;

import com.hospital.appointmentservice.auth.model.UserActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserActivityLogService {
    public void log(String username,String method,String endpoint,long duration,String ip);
    void logActivity(UserActivityLog log);
    Page<UserActivityLog> searchLogs(String username, String action, String endpoint, Pageable pageable);
    void deleteLog(Long id);
    Optional<UserActivityLog> getLogById(Long id);
}
