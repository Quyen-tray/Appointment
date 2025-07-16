package com.hospital.appointmentservice.admin.repository;

import com.hospital.appointmentservice.admin.model.Staff;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StaffAdminRepository extends JpaRepository<Staff, UUID> {
    boolean existsStaffByEmail(String email);

    boolean existsStaffByPhone(@Size(max = 20) String phone);
}
