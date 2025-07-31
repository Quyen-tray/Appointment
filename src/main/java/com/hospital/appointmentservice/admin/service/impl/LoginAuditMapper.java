package com.hospital.appointmentservice.admin.service.impl;

import com.hospital.appointmentservice.auth.dto.LoginAuditDto;
import com.hospital.appointmentservice.auth.model.Login_audit;
import org.springframework.stereotype.Component;

@Component
public class LoginAuditMapper {

    // Entity -> DTO
    public static LoginAuditDto toDto(Login_audit entity) {
        if (entity == null) return null;

        LoginAuditDto dto = new LoginAuditDto();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setEvent(entity.getEvent());
        dto.setTimestamp(entity.getTimestamp());
        dto.setIpAddress(entity.getIpAddress());
        dto.setUserAgent(entity.getUserAgent());
        return dto;
    }

    // DTO -> Entity
    public static Login_audit toEntity(LoginAuditDto dto) {
        if (dto == null) return null;

        Login_audit entity = new Login_audit();
        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
        entity.setEvent(dto.getEvent());
        entity.setTimestamp(dto.getTimestamp());
        entity.setIpAddress(dto.getIpAddress());
        entity.setUserAgent(dto.getUserAgent());
        return entity;
    }
}

