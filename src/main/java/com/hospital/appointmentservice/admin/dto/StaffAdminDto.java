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
public class StaffAdminDto {
    private UUID id;
    private String fullName;

    @Size(max = 10)
    private String gender;
    private LocalDate dob;
    private String email;

    @Size(max = 20)
    private String phone;
    private DepartmentAdminDto departmentAdminDto;
    private PositionAdminDto positionAdminDto;

    @Size(max = 50)
    private String role;


    @Size(max = 50)
    private String status;

    public StaffAdminDto() {
    }

    public StaffAdminDto(String fullName, String gender, LocalDate dob, String email, String phone, DepartmentAdminDto departmentAdminDto, PositionAdminDto positionAdminDto, String status) {
        this.fullName = fullName;
        this.gender = gender;
        this.dob = dob;
        this.email = email;
        this.phone = phone;
        this.departmentAdminDto = departmentAdminDto;
        this.positionAdminDto = positionAdminDto;
        this.status = status;
    }

    public StaffAdminDto(UUID id, String fullName, String gender, LocalDate dob, String email, String phone, DepartmentAdminDto departmentAdminDto, PositionAdminDto positionAdminDto, String status) {
        this.id = id;
        this.fullName = fullName;
        this.gender = gender;
        this.dob = dob;
        this.email = email;
        this.phone = phone;
        this.departmentAdminDto = departmentAdminDto;
        this.positionAdminDto = positionAdminDto;
        this.status = status;
    }

    public StaffAdminDto(String fullName, String gender, LocalDate dob, DepartmentAdminDto departmentAdminDto, PositionAdminDto positionAdminDto,  String status) {
        this.fullName = fullName;
        this.gender = gender;
        this.dob = dob;
        this.departmentAdminDto = departmentAdminDto;
        this.positionAdminDto = positionAdminDto;
        this.status = status;
    }

    public StaffAdminDto(String fullName, String gender, LocalDate dob, String email, DepartmentAdminDto departmentAdminDto, PositionAdminDto positionAdminDto, String status) {
        this.fullName = fullName;
        this.gender = gender;
        this.dob = dob;
        this.email = email;
        this.departmentAdminDto = departmentAdminDto;
        this.positionAdminDto = positionAdminDto;
        this.status = status;
    }


}
