package com.hospital.appointmentservice.doctor.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.appointmentservice.doctor.dto.DepartmentDto;
import com.hospital.appointmentservice.doctor.service.DepartmentService;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {
    private final DepartmentService departmentService ;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/getall-departments")
    public ResponseEntity <?> getAllDepartments(){
        List<DepartmentDto> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

}
