package com.hospital.appointmentservice.admin.controller;

import com.hospital.appointmentservice.admin.dto.CategoryAdminDto;
import com.hospital.appointmentservice.admin.service.CategoryAdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryAdminController {

    @Autowired
    private CategoryAdminService categoryService;

    @GetMapping
    public Page<CategoryAdminDto> search(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        return categoryService.search(keyword, pageable);
    }

    @GetMapping("/{id}")
    public CategoryAdminDto getById(@PathVariable UUID id) {
        return categoryService.getById(id);
    }

    @PostMapping
    public CategoryAdminDto create(@RequestBody @Valid CategoryAdminDto dto) {
        return categoryService.create(dto);
    }

    @PutMapping("/{id}")
    public CategoryAdminDto update(@PathVariable UUID id,
                                   @RequestBody @Valid CategoryAdminDto dto) {
        return categoryService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        categoryService.delete(id);
    }
}
