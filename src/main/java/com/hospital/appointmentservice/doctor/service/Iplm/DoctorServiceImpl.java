package com.hospital.appointmentservice.doctor.service.Iplm;

import java.util.UUID;

import com.hospital.appointmentservice.doctor.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.appointmentservice.admin.model.Doctor;
import com.hospital.appointmentservice.doctor.dto.DoctorDetailDto;
import com.hospital.appointmentservice.doctor.service.DoctorService;
@Service
public class DoctorServiceImpl implements DoctorService{
    @Autowired
    private DoctorRepository doctorRepository ;

    @Override
    public DoctorDetailDto getDoctorDetail(UUID id){
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ!"));

        DoctorDetailDto dto = new DoctorDetailDto();

        //lấy dữ liệu từ bảng staff
        dto.setId(doctor.getId());
        dto.setFullName(doctor.getStaff().getFullName());
        dto.setSpecialty(doctor.getSpecialization());

        //chức vụ mô tả lấy từ position
        if(doctor.getStaff().getPosition() != null){
            dto.setPositionTitle(doctor.getStaff().getPosition().getTitle());
            dto.setDescription(doctor.getStaff().getPosition().getDescription());
        }else
        {
            dto.setPositionTitle("Chưa rõ!");
            dto.setDescription("Không có mô tả!");
        }

        return dto;
    }
}