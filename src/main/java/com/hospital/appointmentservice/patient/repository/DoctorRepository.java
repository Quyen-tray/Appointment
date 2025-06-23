package com.hospital.appointmentservice.patient.repository;

import com.hospital.appointmentservice.admin.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    // thêm query method nếu cần
}
