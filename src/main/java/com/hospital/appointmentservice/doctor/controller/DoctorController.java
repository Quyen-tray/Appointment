package com.hospital.appointmentservice.doctor.controller;

import java.util.List;
import java.util.UUID;


import com.hospital.appointmentservice.doctor.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.appointmentservice.admin.model.Doctor;
import com.hospital.appointmentservice.doctor.dto.DoctorDetailDto;
import com.hospital.appointmentservice.doctor.dto.DoctorListDto;
import com.hospital.appointmentservice.doctor.service.DoctorService;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private final DoctorRepository doctorRepository;

    @Autowired
    private DoctorService doctorService ;

    DoctorController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorDetail(@PathVariable UUID id){
        try {
            DoctorDetailDto dto = doctorService.getDoctorDetail(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/list-doctor")
    public ResponseEntity<?> getAllDcotor(){
        try{
            List<Doctor> doctors = doctorRepository.findAll();
            List<DoctorListDto> result = doctors.stream().map(doc -> new DoctorListDto(doc.getId(),doc.getStaff().getFullName())).toList();
            return ResponseEntity.ok(result);
        }catch(Exception ex){
            return ResponseEntity.status(500).body("Lỗi khi lấy danh sách bác sĩ!");
        }
    }
}