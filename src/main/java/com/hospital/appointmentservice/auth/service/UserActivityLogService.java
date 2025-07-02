package com.hospital.appointmentservice.auth.service;

public interface UserActivityLogService {
    public void log(String username,String method,String endpoint,long duration,String ip);
}
