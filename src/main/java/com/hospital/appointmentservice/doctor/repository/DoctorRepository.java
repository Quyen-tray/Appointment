package com.hospital.appointmentservice.doctor.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.appointmentservice.admin.model.Doctor;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface DoctorRepository extends JpaRepository<Doctor , UUID>{

    List<Doctor> findByStaff_Department_Id(UUID departmentId);

} 
