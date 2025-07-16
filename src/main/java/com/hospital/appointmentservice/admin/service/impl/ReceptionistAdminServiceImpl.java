package com.hospital.appointmentservice.admin.service.impl;

import com.hospital.appointmentservice.admin.dto.ReceptionistAdminDto;
import com.hospital.appointmentservice.admin.model.Receptionist;
import com.hospital.appointmentservice.admin.model.Staff;
import com.hospital.appointmentservice.admin.repository.ReceptionistAdminRepository;
import com.hospital.appointmentservice.admin.service.ReceptionistAdminService;
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
public class ReceptionistAdminServiceImpl implements ReceptionistAdminService {
    @Autowired private ReceptionistAdminRepository receptionistAdminRepository;
    @Autowired private ReceptionistMapper mapper;
    @Override
    public void create(Staff staff) {
        Receptionist receptionist = new Receptionist();
        receptionist.setStaff(staff);
        receptionistAdminRepository.save(receptionist);
    }

    @Override
    public Page<ReceptionistAdminDto> getAll(int page, int size, String sortBy, String direction, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), "staff." + sortBy));

        Specification<Receptionist> spec = (root, query, cb) -> {
            Join<Receptionist, Staff> staff = root.join("staff");
            List<Predicate> predicates = new ArrayList<>();
            if (keyword != null && !keyword.isBlank()) {
                String pattern = "%" + keyword.toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(staff.get("fullName")), pattern));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return receptionistAdminRepository.findAll(spec, pageable).map(mapper::toDto);
    }

    @Override
    public ReceptionistAdminDto getById(UUID id) {
        Receptionist rec = receptionistAdminRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        return mapper.toDto(rec);
    }

    @Override
    public ReceptionistAdminDto update(UUID id, ReceptionistAdminDto dto) {
        Receptionist rec = receptionistAdminRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        mapper.updateEntity(rec, dto);
        return mapper.toDto(receptionistAdminRepository.save(rec));
    }

    @Override
    public void delete(UUID id) {
        receptionistAdminRepository.deleteById(id);
    }
}
