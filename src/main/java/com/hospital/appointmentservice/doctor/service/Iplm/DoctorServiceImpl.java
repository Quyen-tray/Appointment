package com.hospital.appointmentservice.doctor.service.Iplm;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.hospital.appointmentservice.doctor.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.appointmentservice.admin.model.Doctor;
import com.hospital.appointmentservice.doctor.dto.DoctorDetailDto;
import com.hospital.appointmentservice.doctor.dto.DoctorDto;
import com.hospital.appointmentservice.doctor.service.DoctorService;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public DoctorDetailDto getDoctorDetail(UUID id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ!"));

        DoctorDetailDto dto = new DoctorDetailDto();

        // lấy dữ liệu từ bảng staff
        dto.setId(doctor.getId());
        dto.setFullName(doctor.getStaff().getFullName());
        dto.setSpecialty(doctor.getSpecialization());

        // chức vụ mô tả lấy từ position
        if (doctor.getStaff().getPosition() != null) {
            dto.setPositionTitle(doctor.getStaff().getPosition().getTitle());
            dto.setDescription(doctor.getStaff().getPosition().getDescription());
        } else {
            dto.setPositionTitle("Chưa rõ!");
            dto.setDescription("Không có mô tả!");
        }

        return dto;
    }

    @Override
    public List<DoctorDto> getDoctorsByDepartment(UUID departmentId) {
        List<Doctor> doctors = doctorRepository.findByStaff_Department_Id(departmentId);
        return doctors.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private DoctorDto convertToDto(Doctor doctor) {
        DoctorDto dto = new DoctorDto();
        dto.setId(doctor.getId());
        dto.setFullName(doctor.getStaff().getFullName());
        dto.setLicenseNo(doctor.getLicenseNo());
        dto.setSpecialization(doctor.getSpecialization());
        return dto;
    }

}