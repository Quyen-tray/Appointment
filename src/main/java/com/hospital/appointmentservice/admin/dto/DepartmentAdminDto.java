package com.hospital.appointmentservice.admin.dto;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Data
@Getter
@Setter
public class DepartmentAdminDto {
    private UUID id;
    private String name;

    @Size(max = 50)
    private String type;
    private Boolean status;

    public DepartmentAdminDto(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public DepartmentAdminDto(UUID id, String name, String type, Boolean status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
    }
}
