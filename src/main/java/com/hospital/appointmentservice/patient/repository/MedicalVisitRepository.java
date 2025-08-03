package com.hospital.appointmentservice.patient.repository;

import com.hospital.appointmentservice.patient.entity.MedicalVisit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MedicalVisitRepository extends JpaRepository<MedicalVisit, UUID> {
    // Lấy tất cả visits theo patient id
    List<MedicalVisit> findByPatientId(UUID patientId);

    @Query("SELECT mv FROM MedicalVisit mv WHERE mv.patient.id = :patientId " +
            "AND (:fromDate IS NULL OR mv.createdAt >= :fromDate) " +
            "AND (:toDate IS NULL OR mv.createdAt <= :toDate)")
    Page<MedicalVisit> findByPatientIdWithDateFilter(
            @Param("patientId") UUID patientId,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable
    );

    MedicalVisit findByAppointment_Id(UUID appointmentId);
}
