package com.hospital.appointmentservice.auth.service;

import com.hospital.appointmentservice.auth.model.Login_audit;
import com.hospital.appointmentservice.auth.repository.Login_auditRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Service
public class Login_reditServiceImpl implements Login_auditService{
    @Autowired private Login_auditRepository login_auditRepository;

    @Override
    public void log(String username, String event, HttpServletRequest request) {
        Login_audit login_audit = new Login_audit();
        login_audit.setUsername(username);
        login_audit.setEvent(event);
        login_audit.setTimestamp(Instant.from(LocalDateTime.now()));
        login_audit.setIpAddress(request.getRemoteAddr());
        login_audit.setUserAgent(request.getHeader("user-agent"));

        login_auditRepository.save(login_audit);
    }
}
