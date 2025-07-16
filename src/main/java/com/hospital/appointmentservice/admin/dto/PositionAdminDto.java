package com.hospital.appointmentservice.admin.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Data
@Getter
@Setter
public class PositionAdminDto {
    private UUID id;

    @Size(max = 100)
    private String title;
    private CategoryAdminDto categoryAdminDto;
    private Integer rank;
    private String description;

    public PositionAdminDto(UUID id, String title) {
        this.id = id;
        this.title = title;
    }

    public PositionAdminDto(UUID id, String title, CategoryAdminDto categoryAdminDto, Integer rank, String description) {
        this.id = id;
        this.title = title;
        this.categoryAdminDto = categoryAdminDto;
        this.rank = rank;
        this.description = description;
    }
}
