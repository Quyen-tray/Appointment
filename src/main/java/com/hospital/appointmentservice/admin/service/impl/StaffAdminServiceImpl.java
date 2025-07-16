package com.hospital.appointmentservice.admin.service.impl;

import com.hospital.appointmentservice.admin.dto.StaffAdminDto;
import com.hospital.appointmentservice.admin.model.Department;
import com.hospital.appointmentservice.admin.model.Position;
import com.hospital.appointmentservice.admin.model.Staff;
import com.hospital.appointmentservice.admin.repository.DepartmentAdminRepository;
import com.hospital.appointmentservice.admin.repository.PositionAdminRepository;
import com.hospital.appointmentservice.admin.repository.StaffAdminRepository;
import com.hospital.appointmentservice.admin.service.StaffAdminService;
import com.hospital.appointmentservice.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class StaffAdminServiceImpl implements StaffAdminService {
    @Autowired private AuthService authService;
    @Autowired private StaffAdminRepository staffAdminRepository;
    @Autowired private DepartmentAdminRepository departmentRepository;
    @Autowired private PositionAdminRepository positionRepository;

    @Override
    public void addStaff(StaffAdminDto staffAdminDto) {
        Staff staff = new Staff();
        staff.setUserAccount(authService.findByUserAccount(staffAdminDto.getFullName()));
        staffAdminRepository.save(staff);
    }

    @Override
    public Staff getStaff(UUID staffId) {
        return staffAdminRepository.findById(staffId).orElseThrow(()->new RuntimeException("Not found"));
    }

    @Override
    public void updateStaff(StaffAdminDto staffAdminDto) {
        Staff staff = staffAdminRepository.findById(staffAdminDto.getId())
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        staff.setFullName(staffAdminDto.getFullName());
        staff.setGender(staffAdminDto.getGender());
        staff.setDob(staffAdminDto.getDob());
        staff.setEmail(staffAdminDto.getEmail());
        staff.setPhone(staffAdminDto.getPhone());
        staff.setStatus(staffAdminDto.getStatus());

        // Load department
        if (staffAdminDto.getDepartmentAdminDto() != null) {
            Department department = departmentRepository.findById(staffAdminDto.getDepartmentAdminDto().getId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            staff.setDepartment(department);
        } else {
            staff.setDepartment(null); // hoặc giữ nguyên nếu muốn
        }

        // Load position
        if (staffAdminDto.getPositionAdminDto() != null) {
            Position position = positionRepository.findById(staffAdminDto.getPositionAdminDto().getId())
                    .orElseThrow(() -> new RuntimeException("Position not found"));
            staff.setPosition(position);
        } else {
            staff.setPosition(null);
        }

        staffAdminRepository.save(staff);
    }

    @Override
    public boolean existEmail(String email) {
        return staffAdminRepository.existsStaffByEmail(email);
    }

    @Override
    public boolean existPhone(String phone) {
        return staffAdminRepository.existsStaffByPhone(phone);
    }


}
