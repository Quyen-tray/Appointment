package com.hospital.appointmentservice.admin.service;

import com.hospital.appointmentservice.admin.dto.StaffAdminDto;
import com.hospital.appointmentservice.admin.model.Staff;

import java.util.UUID;

public interface StaffAdminService {
    public void addStaff(StaffAdminDto staffAdminDto);
    public Staff getStaff(UUID staffId);
    public void updateStaff(StaffAdminDto staffAdminDto);
    public boolean existEmail(String email);
    public boolean existPhone(String phone);
}
