package com.hospital.appointmentservice.patient.repository;

import com.hospital.appointmentservice.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
Patient findByUser_Username(String username);
    Boolean existsPatientByEmail(String email);
    @EntityGraph(attributePaths = {"medicalVisits"})
    Optional<Patient> findWithVisitsById(UUID id);
    List<Patient> findAllByUser_Id(UUID userId);
    Optional<Patient> findByUser_Id(UUID userId);
    List<Patient> findAllByGender(String gender);
    @Query("SELECT DISTINCT p FROM Patient p JOIN p.appointments a WHERE a.status = :status")
    List<Patient> findAllByAppointmentStatus(@Param("status") String status);

}
