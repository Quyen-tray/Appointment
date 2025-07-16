package com.hospital.appointmentservice.patient.repository;

import com.hospital.appointmentservice.patient.entity.LabRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LabRequestRepository extends JpaRepository<LabRequest, UUID> {

    List<LabRequest> findByVisit_Patient_Id(UUID patientId);

}
