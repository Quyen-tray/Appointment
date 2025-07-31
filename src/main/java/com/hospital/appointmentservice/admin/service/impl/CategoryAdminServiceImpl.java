package com.hospital.appointmentservice.admin.service.impl;

import com.hospital.appointmentservice.admin.dto.CategoryAdminDto;
import com.hospital.appointmentservice.admin.model.Category;

import com.hospital.appointmentservice.admin.repository.CategoryAdminRepository;
import com.hospital.appointmentservice.admin.service.CategoryAdminService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryAdminServiceImpl implements CategoryAdminService {

    @Autowired
    private CategoryAdminRepository categoryRepository;

    private CategoryAdminDto toDto(Category entity) {
        return new CategoryAdminDto(entity.getId(), entity.getName(), entity.getDescription());
    }

    private Category toEntity(CategoryAdminDto dto) {
        Category entity = new Category();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    @Override
    public Page<CategoryAdminDto> search(String keyword, Pageable pageable) {
        Specification<Category> spec = (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;
            String like = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), like),
                    cb.like(cb.lower(root.get("description")), like)
            );
        };

        return categoryRepository.findAll(spec, pageable).map(this::toDto);
    }

    @Override
    public CategoryAdminDto getById(UUID id) {
        Category entity = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        return toDto(entity);
    }

    @Override
    public CategoryAdminDto create(CategoryAdminDto dto) {
        Category saved = categoryRepository.save(toEntity(dto));
        return toDto(saved);
    }

    @Override
    public CategoryAdminDto update(UUID id, CategoryAdminDto dto) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());

        return toDto(categoryRepository.save(existing));
    }

    @Override
    public void delete(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found");
        }
        categoryRepository.deleteById(id);
    }
}
