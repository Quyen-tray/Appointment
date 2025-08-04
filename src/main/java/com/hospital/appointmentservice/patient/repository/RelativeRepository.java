package com.hospital.appointmentservice.patient.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.appointmentservice.patient.entity.Relative;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RelativeRepository extends JpaRepository<Relative, UUID> {
    List<Relative> findByPatient_Id(UUID patientId);

    Optional<Relative> findByIdAndPatient_User_Username(UUID id, String username);

    Page<Relative> findByPatient_Id(UUID patientId, Pageable pageable);

    Page<Relative> findByPatient_IdAndFullNameContainingIgnoreCase(UUID patientId, String fullName, Pageable pageable);

}
