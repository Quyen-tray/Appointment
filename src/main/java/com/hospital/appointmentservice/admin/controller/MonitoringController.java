package com.hospital.appointmentservice.admin.controller;

import com.hospital.appointmentservice.admin.service.impl.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/monitoring")
public class MonitoringController {

    @Autowired
    private MonitoringService monitoringService;

    @GetMapping("/login-stats")
    public Map<String, Object> getLoginStats() {
        return Map.of(
                "today", monitoringService.countLoginSuccessToday(),
                "week", monitoringService.countLoginSuccessThisWeek(),
                "month", monitoringService.countLoginSuccessThisMonth(),
                "failed", monitoringService.countLoginFailed(),
                "firstLogins", monitoringService.countFirstLogins()
        );
    }

    @GetMapping("/recent-logins")
    public List<Map<String, Object>> getRecentLogins() {
        return monitoringService.getRecentLogins(10);
    }

    @GetMapping("/top-endpoints")
    public List<Map<String, Object>> getTopEndpoints() {
        return monitoringService.getTopEndpoints(5);
    }

    @GetMapping("/api-calls-daily")
    public List<Map<String, Object>> getApiCallsByDay() {
        return monitoringService.getApiCallsByDay();
    }

    @GetMapping("/average-duration")
    public List<Map<String, Object>> getAverageDurationPerEndpoint() {
        return monitoringService.getAverageDurationPerEndpoint();
    }

    @GetMapping("/request-methods")
    public List<Map<String, Object>> getRequestCountByMethod() {
        return monitoringService.getRequestCountByMethod();
    }
}
