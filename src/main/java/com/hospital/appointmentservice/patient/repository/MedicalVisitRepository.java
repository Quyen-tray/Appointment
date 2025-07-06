package com.hospital.appointmentservice.patient.repository;

import com.hospital.appointmentservice.patient.entity.MedicalVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicalVisitRepository extends JpaRepository<MedicalVisit, UUID> {
    // Lấy tất cả visits theo patient id
    List<MedicalVisit> findByPatientId(UUID patientId);
}
