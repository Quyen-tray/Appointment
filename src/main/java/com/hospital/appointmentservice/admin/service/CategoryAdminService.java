package com.hospital.appointmentservice.admin.service;

import com.hospital.appointmentservice.admin.dto.CategoryAdminDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CategoryAdminService {
    Page<CategoryAdminDto> search(String keyword, Pageable pageable);

    CategoryAdminDto getById(UUID id);

    CategoryAdminDto create(CategoryAdminDto dto);

    CategoryAdminDto update(UUID id, CategoryAdminDto dto);

    void delete(UUID id);
}
