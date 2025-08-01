package com.hospital.appointmentservice.doctor.service;

import java.util.List;
import java.util.UUID;

import com.hospital.appointmentservice.doctor.dto.DoctorDetailDto;
import com.hospital.appointmentservice.doctor.dto.DoctorDto;


public interface DoctorService {
    DoctorDetailDto getDoctorDetail(UUID id);
    List<DoctorDto> getDoctorsByDepartment(UUID departmentId);
}