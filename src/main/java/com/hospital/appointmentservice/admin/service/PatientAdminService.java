package com.hospital.appointmentservice.admin.service;

import com.hospital.appointmentservice.admin.dto.PatientAdminDto;
import com.hospital.appointmentservice.auth.dto.UserAccountDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface PatientAdminService {
    PatientAdminDto create(PatientAdminDto dto);
    Page<PatientAdminDto> getAll(int page, int size, String sortBy, String direction, String keyword);
    PatientAdminDto update(UUID id, PatientAdminDto dto);
    void delete(UUID id);
    PatientAdminDto getById(UUID id);
    boolean existsPatientByPhone(String phone);
}
