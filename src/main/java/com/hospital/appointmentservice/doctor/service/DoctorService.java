package com.hospital.appointmentservice.doctor.service;

import java.util.UUID;
import com.hospital.appointmentservice.doctor.dto.DoctorDetailDto;


public interface DoctorService {
    DoctorDetailDto getDoctorDetail(UUID id);
}