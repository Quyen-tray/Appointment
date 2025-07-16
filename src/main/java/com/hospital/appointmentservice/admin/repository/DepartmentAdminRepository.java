package com.hospital.appointmentservice.admin.repository;

import com.hospital.appointmentservice.admin.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DepartmentAdminRepository extends JpaRepository<Department, UUID> {
}
