package com.hospital.appointmentservice.auth.service;

import jakarta.servlet.http.HttpServletRequest;

public interface Login_auditService {
    public void log(String username, String event, HttpServletRequest request);
}
