package com.hospital.appointmentservice.admin.service.impl;

import com.hospital.appointmentservice.admin.dto.DepartmentAdminDto;
import com.hospital.appointmentservice.admin.dto.PositionAdminDto;
import com.hospital.appointmentservice.admin.dto.ReceptionistAdminDto;
import com.hospital.appointmentservice.admin.dto.StaffAdminDto;
import com.hospital.appointmentservice.admin.model.Department;
import com.hospital.appointmentservice.admin.model.Position;
import com.hospital.appointmentservice.admin.model.Receptionist;
import com.hospital.appointmentservice.admin.model.Staff;
import com.hospital.appointmentservice.admin.service.StaffAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReceptionistMapper {
    @Autowired private StaffAdminService staffAdminService;
    public ReceptionistAdminDto toDto(Receptionist entity) {
        Staff staff = entity.getStaff();

        Department department = staff.getDepartment();
        Position position = staff.getPosition();

        DepartmentAdminDto departmentDto = (department != null)
                ? new DepartmentAdminDto(department.getId(), department.getName())
                : null;

        PositionAdminDto positionDto = (position != null)
                ? new PositionAdminDto(position.getId(), position.getTitle())
                : null;

        return new ReceptionistAdminDto(
                staff.getId(),
                staff.getFullName(),
                staff.getGender(),
                staff.getDob(),
                staff.getEmail(),
                staff.getPhone(),
                departmentDto,
                positionDto,
                staff.getStatus(),
                entity.getNote()
        );
    }


    public void updateEntity(Receptionist entity, ReceptionistAdminDto dto) {
        entity.setNote(dto.getNote());
        // Nếu muốn cập nhật Staff thì update thông qua StaffService
        StaffAdminDto staffAdminDto = new StaffAdminDto();
        staffAdminDto.setId(dto.getId());
        staffAdminDto.setFullName(dto.getFullName());
        staffAdminDto.setGender(dto.getGender());
        staffAdminDto.setDob(dto.getDob());
        if(!staffAdminService.existEmail(dto.getEmail())) {
            staffAdminDto.setEmail(dto.getEmail());
        }

        if(!staffAdminService.existPhone(dto.getPhone())) {
            staffAdminDto.setPhone(dto.getPhone());
        }

        staffAdminDto.setDepartmentAdminDto(dto.getDepartmentAdminDto());
        staffAdminDto.setPositionAdminDto(dto.getPositionAdminDto());
        staffAdminDto.setStatus(dto.getStatus());

        staffAdminService.updateStaff(staffAdminDto);
    }
}

