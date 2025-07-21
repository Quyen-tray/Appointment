package com.hospital.appointmentservice.patient.repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.appointmentservice.admin.model.Doctor;
import com.hospital.appointmentservice.admin.model.Appointment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentRepository extends JpaRepository<Appointment , UUID> {
    List<Appointment> findByPatient_Id(UUID patientid);

    boolean existsByDoctorAndScheduledTime(Doctor doctor, java.time.LocalDateTime scheduledTime);

    @Query(value = """
    SELECT a.* FROM appointment a
    JOIN patient p ON a.patient_id = p.patient_id
    WHERE (:keyword IS NULL OR :keyword = '' OR
           p.full_name COLLATE Latin1_General_CI_AI LIKE CONCAT('%', :keyword, '%'))
      AND (:status IS NULL OR a.status = :status)
    ORDER BY
      CASE 
        WHEN :isIncreaseScheduleDate = 1 THEN a.scheduled_time
      END ASC,
      CASE 
        WHEN :isIncreaseScheduleDate = 0 THEN a.scheduled_time
      END DESC
    """, nativeQuery = true)
    List<Appointment> searchAppointments(
            @Param("keyword") String keyword,
            @Param("status") String status,
            @Param("isIncreaseScheduleDate") Boolean isIncreaseScheduleDate);

}