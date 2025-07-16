package com.hospital.appointmentservice.admin.repository;

import com.hospital.appointmentservice.admin.model.Room;
import com.hospital.appointmentservice.admin.model.Staff;
import com.hospital.appointmentservice.auth.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StaffRepository extends JpaRepository<Staff, UUID> {
    Staff findByUserAccount(UserAccount userAccount);
}
