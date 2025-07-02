package com.hospital.appointmentservice.patient.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.appointmentservice.admin.model.Doctor;
import com.hospital.appointmentservice.admin.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment , UUID> {
     List<Appointment> findByPatient_Id(UUID patientid);

    boolean existsByDoctorAndScheduledTime(Doctor doctor , java.time.LocalDateTime scheduledTime);
}
