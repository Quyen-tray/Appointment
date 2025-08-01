package com.hospital.appointmentservice.receptionist.service;


import com.hospital.appointmentservice.admin.model.Doctor;
import com.hospital.appointmentservice.doctor.repository.DoctorRepository;
import com.hospital.appointmentservice.receptionist.dto.DoctorResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceptionistDoctorServiceImpl implements ReceptionistDoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public List<DoctorResponseDTO> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream().map(doc -> new DoctorResponseDTO(
                doc.getId(),
                doc.getFullName(),
                doc.getStaff().getEmail(),
                doc.getStaff().getPhone(),
                doc.getSpecialization(),
                doc.getStaff().getDepartment() != null ? doc.getStaff().getDepartment().getName() : "Chưa có",
                doc.getStaff().getStatus()
        )).collect(Collectors.toList());
    }
}