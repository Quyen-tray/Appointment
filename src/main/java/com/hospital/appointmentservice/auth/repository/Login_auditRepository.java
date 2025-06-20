package com.hospital.appointmentservice.auth.repository;

import com.hospital.appointmentservice.auth.model.Login_audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Login_auditRepository extends JpaRepository<Login_audit,Long> {
}
