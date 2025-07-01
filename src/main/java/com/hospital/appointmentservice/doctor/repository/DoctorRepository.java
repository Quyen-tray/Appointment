package com.hospital.appointmentservice.doctor.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.appointmentservice.admin.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor , UUID>{

    
} 
