package com.hospital.appointmentservice.admin.service.impl;

import com.hospital.appointmentservice.admin.dto.PatientAdminDto;
import com.hospital.appointmentservice.admin.repository.PatientAdminRepository;
import com.hospital.appointmentservice.admin.service.PatientAdminService;
import com.hospital.appointmentservice.auth.dto.UserAccountDto;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.service.PatientService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PatientAdminServiceImpl implements PatientAdminService {
    @Autowired private PatientAdminRepository patientAdminRepository;
    @Autowired private PatientService patientService;
    private PatientAdminDto toDto(Patient patient) {
        return new PatientAdminDto(
                patient.getId(),
                new UserAccountDto(patient.getUser().getId(),patient.getUser().getUsername()),
                patient.getFullName(),
                patient.getDob(),
                patient.getGender(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getInsuranceId()
        );
    }

    private  Patient toEntity(PatientAdminDto dto) {
        Patient patient = new Patient();
        patient.setFullName(dto.getFullName());
        patient.setDob(dto.getDob());
        patient.setGender(dto.getGender());
        patient.setPhone(dto.getPhone());
        patient.setEmail(dto.getEmail());
        patient.setInsuranceId(dto.getInsuranceId());
        patient.setAvatar(dto.getAvatar());
        return patient;
    }

    @Override
    public PatientAdminDto create(PatientAdminDto dto) {
        return null;
    }

    @Override
    public Page<PatientAdminDto> getAll(int page, int size, String sortBy, String direction, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));

        Specification<Patient> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Join với userAccount
            Join<Object, Object> userJoin = root.join("user"); // field này là Patient.user (userAccount)

            // Thêm điều kiện status = 'ACTIVE'
            predicates.add(cb.equal(cb.lower(userJoin.get("status")), "active"));

            // Nếu có keyword thì lọc theo email
            if (keyword != null && !keyword.isEmpty()) {
                String likePattern = "%" + keyword.toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("email")), likePattern));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return patientAdminRepository.findAll(spec, pageable).map(this::toDto);
    }


    @Override
    public PatientAdminDto update(UUID id, PatientAdminDto dto) {
        Patient patient = patientAdminRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        patient.setFullName(dto.getFullName());
        patient.setDob(dto.getDob());
        patient.setGender(dto.getGender());
        if(!existsPatientByPhone(dto.getPhone())){
            patient.setPhone(dto.getPhone());
        }
        if(!patientService.existsPatientByEmail(dto.getEmail())) {
            patient.setEmail(dto.getEmail());
        }
        patient.setInsuranceId(dto.getInsuranceId());
        patient.setAvatar(dto.getAvatar());
        return toDto(patientAdminRepository.save(patient));
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public PatientAdminDto getById(UUID id) {
        return patientAdminRepository.findById(id).map(this::toDto).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override
    public boolean existsPatientByPhone(String phone) {
        return patientAdminRepository.existsPatientByPhone(phone);
    }


}
