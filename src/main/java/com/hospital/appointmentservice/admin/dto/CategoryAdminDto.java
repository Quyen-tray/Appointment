package com.hospital.appointmentservice.admin.dto;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Data
@Getter
@Setter
public class CategoryAdminDto {
    private UUID id;

    @Size(max = 100)
    private String name;
    private String description;

    public CategoryAdminDto(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryAdminDto(UUID id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
