package com.hospital.appointmentservice.receptionist.repository;

import com.hospital.appointmentservice.patient.entity.MedicalVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicalVisitRepRepository extends JpaRepository<MedicalVisit, UUID> {
    List<MedicalVisit> findByPatientId(UUID patientId);
}
