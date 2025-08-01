package com.hospital.appointmentservice.doctor.service.Iplm;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hospital.appointmentservice.doctor.dto.DepartmentDto;
import com.hospital.appointmentservice.doctor.repository.DepartmentRepository;
import com.hospital.appointmentservice.doctor.service.DepartmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Override
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(dept -> new DepartmentDto(dept.getId(), dept.getName())).collect(Collectors.toList());
    }
}
