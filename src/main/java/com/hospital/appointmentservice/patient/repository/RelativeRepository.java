package com.hospital.appointmentservice.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.appointmentservice.patient.entity.Relative;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RelativeRepository extends JpaRepository<Relative, UUID> {
    List<Relative> findByPatient_Id(UUID patientId);
    Optional<Relative> findByIdAndPatient_User_Username(UUID id , String username);
}
