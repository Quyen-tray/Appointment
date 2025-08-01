package com.hospital.appointmentservice.admin.repository;

import com.hospital.appointmentservice.admin.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryAdminRepository extends JpaRepository<Category, UUID>, JpaSpecificationExecutor<Category> {
}
