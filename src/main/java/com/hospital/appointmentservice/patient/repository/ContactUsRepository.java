package com.hospital.appointmentservice.patient.repository;

import com.hospital.appointmentservice.patient.entity.ContactUs;
import com.hospital.appointmentservice.patient.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ContactUsRepository extends JpaRepository<ContactUs, UUID> {
}
