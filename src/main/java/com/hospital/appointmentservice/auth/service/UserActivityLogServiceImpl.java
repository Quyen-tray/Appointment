package com.hospital.appointmentservice.auth.service;

import com.hospital.appointmentservice.auth.model.UserActivityLog;
import com.hospital.appointmentservice.auth.repository.UserActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

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
}
