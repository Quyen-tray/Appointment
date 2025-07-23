package com.hospital.appointmentservice.admin.service;

import com.hospital.appointmentservice.admin.dto.ReceptionistAdminDto;
import com.hospital.appointmentservice.admin.model.Staff;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ReceptionistAdminService {
    void create(Staff staff);
    Page<ReceptionistAdminDto> getAll(int page, int size, String sortBy, String direction, String keyword);
    ReceptionistAdminDto update(UUID id, ReceptionistAdminDto dto);
    void delete(UUID id);
    ReceptionistAdminDto getById(UUID id);
}
