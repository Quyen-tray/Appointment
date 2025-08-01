package com.hospital.appointmentservice.auth.service;

import com.hospital.appointmentservice.auth.dto.LoginAuditDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface LoginAuditService {
    Page<LoginAuditDto> search(int page, int size, String sort, String username, String event, String ip,
                               LocalDateTime from, LocalDateTime to);

    Map<String, Object> getLoginStats();

    List<LoginAuditDto> getRecentLogins(int limit);

    void exportToExcel(HttpServletResponse response) throws IOException;
}
