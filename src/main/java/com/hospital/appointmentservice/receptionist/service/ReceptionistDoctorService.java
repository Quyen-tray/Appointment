package com.hospital.appointmentservice.receptionist.service;

import com.hospital.appointmentservice.receptionist.dto.DoctorResponseDTO;

import java.util.List;

public interface ReceptionistDoctorService {
    List<DoctorResponseDTO> getAllDoctors();
}
