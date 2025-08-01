package com.hospital.appointmentservice.doctor.service;

import java.util.List;

import com.hospital.appointmentservice.doctor.dto.DepartmentDto;

public interface DepartmentService {

    List<DepartmentDto> getAllDepartments();
}
