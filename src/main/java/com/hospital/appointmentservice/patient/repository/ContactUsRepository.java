package com.hospital.appointmentservice.patient.repository;

import com.hospital.appointmentservice.patient.entity.ContactUs;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ContactUsRepository extends JpaRepository<ContactUs, UUID> {
}
