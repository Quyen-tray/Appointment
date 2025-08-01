package com.hospital.appointmentservice.doctor.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.appointmentservice.admin.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,UUID>{
    
}
