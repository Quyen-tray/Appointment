package com.hospital.appointmentservice.patient.repository;

import com.hospital.appointmentservice.patient.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
