package com.hospital.appointmentservice.patient.repository;

import com.hospital.appointmentservice.patient.entity.LabRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LabRequestRepository extends JpaRepository<LabRequest, UUID> {

    /**
     * Spring Data JPA method dựa trên tên phương thức:
     * findByVisit_Patient_Id: join visit -> patient -> id
     *
     * Cách này tự động sinh query:
     * SELECT l FROM LabRequest l
     * JOIN l.visit mv
     * WHERE mv.patient.id = :patientId
     */
    List<LabRequest> findByVisit_Patient_Id(UUID patientId);

    // Hoặc nếu tên trường khác, ví dụ LabRequest.visit, MedicalVisit.patient:
    // List<LabRequest> findByVisit_Patient_PatientId(UUID patientId);

    // Nếu muốn custom query (JPQL):
    // @Query("SELECT l FROM LabRequest l JOIN l.visit mv WHERE mv.patient.id = :patientId")
    // List<LabRequest> findByPatientId(@Param("patientId") UUID patientId);
}
