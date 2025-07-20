package com.hospital.appointmentservice.receptionist.repository;

import com.hospital.appointmentservice.receptionist.entity.MedicalRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, UUID> {

    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.appointment.patient.id = :patientId")
    List<MedicalRecord> findByPatientId(@Param("patientId") UUID patientId);
}

