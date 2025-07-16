package com.hospital.appointmentservice.admin.dto;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;
@Data
@Getter
@Setter
public class ReceptionistAdminDto {
    private UUID id;
    private String fullName;

    @Size(max = 10)
    private String gender;
    private LocalDate dob;
    private String email;

    @Size(max = 20)
    private String phone;

    @Size(max = 50)
    private String role;
    private DepartmentAdminDto departmentAdminDto;
    private PositionAdminDto positionAdminDto;


    @Size(max = 50)
    private String status;
    private String note;

    public ReceptionistAdminDto() {
    }

    public ReceptionistAdminDto(UUID id, String note) {
        this.id = id;
        this.note = note;
    }

    public ReceptionistAdminDto(UUID id, String fullName, String gender, LocalDate dob, String email, String phone, DepartmentAdminDto departmentAdminDto, PositionAdminDto positionAdminDto, String status, String note) {
        this.id = id;
        this.fullName = fullName;
        this.gender = gender;
        this.dob = dob;
        this.email = email;
        this.phone = phone;
        this.departmentAdminDto = departmentAdminDto;
        this.positionAdminDto = positionAdminDto;
        this.status = status;
        this.note = note;
    }

    public ReceptionistAdminDto(UUID id, String fullName, String gender, LocalDate dob, String email, String phone, String status, String note) {
        this.id = id;
        this.fullName = fullName;
        this.gender = gender;
        this.dob = dob;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.note = note;
    }


}
