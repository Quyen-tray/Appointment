package com.hospital.appointmentservice.patient.repository;

import com.hospital.appointmentservice.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
Patient findByUser_Username(String username);
    Boolean existsPatientByEmail(String email);
    @EntityGraph(attributePaths = {"medicalVisits"})
    Optional<Patient> findWithVisitsById(UUID id);
}
