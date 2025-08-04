package com.hospital.appointmentservice.patient.service.iplm;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.hospital.appointmentservice.patient.dto.RelativeDto;
import com.hospital.appointmentservice.patient.dto.RelativeResponseDto;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.entity.Relative;
import com.hospital.appointmentservice.patient.repository.PatientRepository;
import com.hospital.appointmentservice.patient.repository.RelativeRepository;
import com.hospital.appointmentservice.patient.service.RelativeService;

@Service
public class RelativeServiceImpl implements RelativeService {

    @Autowired
    private RelativeRepository relativeRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public List<RelativeDto> getRelativesByUsername(String username) {
        Patient patient = patientRepository.findByUser_Username(username);
        List<Relative> relatives = relativeRepository.findByPatient_Id(patient.getId());

        return relatives.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public RelativeDto addRelative(String username, RelativeDto dto) {
        Patient patient = patientRepository.findByUser_Username(username);

        Relative relative = new Relative();
        relative.setPatient(patient);
        relative.setFullName(dto.getFullName());
        relative.setDob(dto.getDob());
        relative.setGender(dto.getGender());
        relative.setRelation(dto.getRelation());
        relative.setNote(dto.getNote());

        Relative saved = relativeRepository.save(relative);
        return toDto(saved);
    }

    @Override
    public RelativeDto updateRelative(UUID id, RelativeDto dto, String username) {
        Optional<Relative> optional = relativeRepository.findByIdAndPatient_User_Username(id, username);

        if (optional.isEmpty()) {
            throw new RuntimeException("Không tìm thấy người thân hợp lệ!");
        }

        Relative relative = optional.get();
        relative.setFullName(dto.getFullName());
        relative.setDob(dto.getDob());
        relative.setGender(dto.getGender());
        relative.setNote(dto.getNote());
        relative.setRelation(dto.getRelation());

        Relative updated = relativeRepository.save(relative);
        return toDto(updated);
    }

    @Override
    public void deleteRelative(UUID id, String username) {
        Optional<Relative> optional = relativeRepository.findByIdAndPatient_User_Username(id, username);
        if (optional.isEmpty()) {
            throw new RuntimeException("Không tìm thấy người thân để xóa!");
        }
        relativeRepository.deleteById(id);
    }

    private RelativeDto toDto(Relative relative) {
        RelativeDto dto = new RelativeDto();
        dto.setId(relative.getId());
        dto.setFullName(relative.getFullName());
        dto.setDob(relative.getDob());
        dto.setGender(relative.getGender());
        dto.setRelation(relative.getRelation());
        dto.setNote(relative.getNote());
        return dto;
    }

    @Override
    public List<RelativeResponseDto> getRelativesSummaryByUsername(String username) {
        Patient patient = patientRepository.findByUser_Username(username);
        if (patient == null) {
            throw new RuntimeException("Không tìm thấy bệnh nhân");
        }

        List<Relative> relatives = relativeRepository.findByPatient_Id(patient.getId());

        return relatives.stream()
                .map(r -> new RelativeResponseDto(r.getId(), r.getFullName(), r.getRelation()))
                .toList();
    }

    @Override
    public Page<RelativeDto> getPagedRelativesWithFullInfo(String username, int page, int size, String search) {
        Patient patient = patientRepository.findByUser_Username(username);
        if (patient == null) {
            throw new RuntimeException("Không tìm thấy bệnh nhân");
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Relative> relativePage;
        if (search != null && !search.isEmpty()) {
            relativePage = relativeRepository.findByPatient_IdAndFullNameContainingIgnoreCase(
                    patient.getId(), search, pageable);
        } else {
            relativePage = relativeRepository.findByPatient_Id(patient.getId(), pageable);
        }

        return relativePage.map(this::toDto);
    }

}
