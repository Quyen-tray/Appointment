package com.hospital.appointmentservice.patient.service.iplm;

import com.hospital.appointmentservice.doctor.repository.DoctorRepository;
import com.hospital.appointmentservice.patient.service.FeedBackService;
import com.hospital.appointmentservice.patient.dto.FeedBackDto;
import com.hospital.appointmentservice.patient.entity.FeedBack;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.repository.FeedBackRepository;
import com.hospital.appointmentservice.patient.repository.PatientRepository;
import com.hospital.appointmentservice.admin.model.Doctor;
import com.hospital.appointmentservice.doctor.repository.DoctorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FeedBackServiceImpl implements FeedBackService {

    private final int pageSize = 100;
    private final FeedBackRepository feedBackRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;


    @Autowired
    public FeedBackServiceImpl(FeedBackRepository feedBackRepository,
                               PatientRepository patientRepository,
                               DoctorRepository doctorRepository) {
        this.feedBackRepository = feedBackRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public List<FeedBackDto> getAllFeedBack() {
        return feedBackRepository.findAll().stream()
      }
  
    public List<FeedBackDto> getAllFeedBack(int page) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "dateCreate"));
        return feedBackRepository.findAll(pageable).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public FeedBackDto getFeedBackById(UUID id) {
        return feedBackRepository.findById(id)
                .map(this::mapToDto)
                .orElse(null);
    }

    @Override
    public List<FeedBackDto> getFeedBackByPatientId(UUID patientId) {
        if (!patientRepository.existsById(patientId)) {
            return null;
        }
        return feedBackRepository.findByPatientId(patientId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedBackDto> getFeedBackByDoctorId(UUID doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            return null;
        }
        return feedBackRepository.findByDoctorId(doctorId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public FeedBackDto createFeedBack(FeedBackDto dto) {
        UUID pid, did;
        try {
            pid = UUID.fromString(dto.getPatientId());
            did = UUID.fromString(dto.getDoctorId());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format");
        }
        Patient patient = patientRepository.findById(pid)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + dto.getPatientId()));
        Doctor doctor = doctorRepository.findById(did)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + dto.getDoctorId()));

        FeedBack fb = new FeedBack();
        fb.setPatient(patient);
        fb.setDoctor(doctor);
        fb.setScore(dto.getScore());
        fb.setComment(dto.getComment());

        FeedBack saved = feedBackRepository.save(fb);
        return mapToDto(saved);
    }

    @Override
    public FeedBackDto updateFeedBack(UUID id, FeedBackDto dto) {
        return feedBackRepository.findById(id).map(existing -> {
            if (dto.getScore() != null) {
                existing.setScore(dto.getScore());
            }
            if (dto.getComment() != null) {
                existing.setComment(dto.getComment());
            }
            FeedBack updated = feedBackRepository.save(existing);
            return mapToDto(updated);
        }).orElse(null);
    }

    @Override
    public boolean deleteFeedBack(UUID id) {
        if (!feedBackRepository.existsById(id)) {
            return false;
        }
        feedBackRepository.deleteById(id);
        return true;
    }

    private FeedBackDto mapToDto(FeedBack entity) {
        FeedBackDto dto = new FeedBackDto();
        dto.setId(entity.getId().toString());
        if (entity.getPatient() != null) dto.setPatientId(entity.getPatient().getId().toString());
        if (entity.getDoctor() != null) dto.setDoctorId(entity.getDoctor().getId().toString());
        dto.setScore(entity.getScore());
        dto.setComment(entity.getComment());
        return dto;
    }
}
