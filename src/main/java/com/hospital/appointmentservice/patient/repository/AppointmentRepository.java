package com.hospital.appointmentservice.patient.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.appointmentservice.admin.model.Doctor;
import com.hospital.appointmentservice.admin.model.Appointment;
import com.hospital.appointmentservice.admin.model.Department;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
  List<Appointment> findByPatient_Id(UUID patientid);

  Page<Appointment> findByPatient_Id(UUID patientId, Pageable pageable);

  boolean existsByDoctorAndScheduledTime(Doctor doctor, java.time.LocalDateTime scheduledTime);

  Page<Appointment> findByPatient_IdAndDoctor_Staff_FullNameContainingIgnoreCase(UUID patientId, String doctorName,
      Pageable pageable);

  // lọc cho patient history
  @Query("""
          SELECT a FROM Appointment a
          LEFT JOIN a.relative r
          JOIN a.doctor d
          JOIN d.staff s
          WHERE a.patient.id = :patientId
            AND (:doctorName IS NULL OR LOWER(s.fullName) LIKE LOWER(CONCAT('%', :doctorName, '%')))
            AND (:startDate IS NULL OR a.scheduledTime >= :startDate)
            AND (:endDate IS NULL OR a.scheduledTime <= :endDate)
            AND (:status IS NULL OR a.status = :status)
            AND (
              :examiner IS NULL OR :examiner = 'Tất cả' OR
              (:examiner = 'Bản thân' AND r IS NULL) OR
              (r IS NOT NULL AND CONCAT(r.fullName, ' (', r.relation, ')') = :examiner)
            )
      """)
  Page<Appointment> findByFilters(
      @Param("patientId") UUID patientId,
      @Param("doctorName") String doctorName,
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate,
      @Param("status") String status,
      @Param("examiner") String examiner,
      Pageable pageable);

  // kiem tra lich cua ban than
  boolean existsByPatient_IdAndDoctor_Staff_DepartmentAndStatusIn(
      UUID patientId, Department department, List<String> statuses);

  boolean existsByRelative_IdAndDoctor_Staff_DepartmentAndStatusIn(
      UUID relativeId, Department department, List<String> statuses);

  // Kiểm tra patient có lịch trùng thời điểm
  boolean existsByPatient_IdAndScheduledTime(UUID patientId, LocalDateTime time);

  // Kiểm tra relative có lịch trùng thời điểm
  boolean existsByRelative_IdAndScheduledTime(UUID relativeId, LocalDateTime time);

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

  boolean existsByScheduledTimeAndRoom_Id(LocalDateTime scheduledTime, UUID roomId);

  boolean existsByScheduledTimeAndDoctor_Id(LocalDateTime scheduledTime, UUID doctorId);

  boolean existsByScheduledTimeAndPatient_Id(LocalDateTime scheduledTime, UUID patientId);

  // Với update, cần loại trừ chính appointment hiện tại
  boolean existsByScheduledTimeAndRoom_IdAndIdNot(LocalDateTime scheduledTime, UUID roomId, UUID appointmentId);

  boolean existsByScheduledTimeAndDoctor_IdAndIdNot(LocalDateTime scheduledTime, UUID doctorId, UUID appointmentId);


  @Query("""
    SELECT a FROM Appointment a
    WHERE a.doctor.id = :doctor_id
      
      AND a.status = "APPROVED"
""")
  List<Appointment> findAppointmentsByDoctorAndDay(
          @Param("doctor_id") UUID doctorId,
          @Param("startOfDay") LocalDateTime startOfDay,
          @Param("endOfDay") LocalDateTime endOfDay
  );
//AND a.scheduledTime BETWEEN :startOfDay AND :endOfDay
  boolean existsByScheduledTimeAndPatient_IdAndIdNot(LocalDateTime scheduledTime, UUID patientId, UUID appointmentId);
}