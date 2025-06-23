package com.hospital.appointmentservice.patient.repository;

import com.hospital.appointmentservice.patient.entity.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface FeedBackRepository extends JpaRepository<FeedBack, UUID> {
    List<FeedBack> findByPatientId(UUID patientId);
    List<FeedBack> findByDoctorId(UUID doctorId);
}
