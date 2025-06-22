package com.hospital.appointmentservice.patient.service.iplm;

import com.hospital.appointmentservice.patient.dto.MedicalVisitDto;
import com.hospital.appointmentservice.patient.entity.MedicalVisit;
import com.hospital.appointmentservice.patient.repository.MedicalVisitRepository;
import com.hospital.appointmentservice.patient.repository.PatientRepository;
import com.hospital.appointmentservice.patient.service.MedicalVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
public class MedicalVisitServiceImpl implements MedicalVisitService {
    private final MedicalVisitRepository medicalVisitRepository;
    private final PatientRepository patientRepository; // để kiểm tra tồn tại patient khi tạo mới
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Autowired
    public MedicalVisitServiceImpl(MedicalVisitRepository medicalVisitRepository,
                                   PatientRepository patientRepository) {
        this.medicalVisitRepository = medicalVisitRepository;
        this.patientRepository = patientRepository;
    }

    // Helper: map entity -> DTO
    private MedicalVisitDto mapToDto(MedicalVisit mv) {
        MedicalVisitDto dto = new MedicalVisitDto();
        dto.setId(mv.getId().toString());
        if (mv.getAppointment() != null && mv.getAppointment().getId() != null) {
            dto.setAppointmentId(mv.getAppointment().getId().toString());
        }
        if (mv.getDoctor() != null && mv.getDoctor().getId() != null) {
            dto.setDoctorId(mv.getDoctor().getId().toString());
        }
        if (mv.getPatient() != null && mv.getPatient().getId() != null) {
            dto.setPatientId(mv.getPatient().getId().toString());
        }
        dto.setDiagnosis(mv.getDiagnosis());
        dto.setNote(mv.getNote());
        if (mv.getCreatedAt() != null) {
            dto.setCreatedAt(mv.getCreatedAt().format(dateFormatter));
        }
        return dto;
    }

    // Helper: map DTO -> entity (cho create/update). Cần load Appointment/Doctor/Patient từ DB nếu cần.
    // Ví dụ đơn giản chỉ set các trường text; nếu relationship, cần fetch các entity Appointment, Doctor, Patient.
    private void mapToEntity(MedicalVisit mv, MedicalVisitDto dto) {
        // Ví dụ: chỉ set diagnosis, note, createdAt; relationship cần fetch entity khác:
        mv.setDiagnosis(dto.getDiagnosis());
        mv.setNote(dto.getNote());
        // Nếu createdAt là String và bạn muốn parse:
        // mv.setCreatedAt(LocalDateTime.parse(dto.getCreatedAt(), dateFormatter));
        // Relationship: nếu dto.getPatientId()!=null, load Patient entity và set vào mv.setPatient(...)
        // Cần inject PatientRepository, AppointmentRepository, DoctorRepository nếu muốn create/update relation.
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalVisitDto> getAllVisits() {
        List<MedicalVisit> list = medicalVisitRepository.findAll();
        List<MedicalVisitDto> dtos = new ArrayList<>();
        for (MedicalVisit mv : list) {
            dtos.add(mapToDto(mv));
        }
        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalVisitDto getVisitById(UUID id) {
        Optional<MedicalVisit> opt = medicalVisitRepository.findById(id);
        if (opt.isEmpty()) {
            return null;
        }
        return mapToDto(opt.get());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalVisitDto> getVisitsByPatientId(UUID patientId) {
        // Có thể kiểm tra patient tồn tại:
        if (!patientRepository.existsById(patientId)) {
            return null;
        }
        List<MedicalVisit> list = medicalVisitRepository.findByPatientId(patientId);
        List<MedicalVisitDto> dtos = new ArrayList<>();
        for (MedicalVisit mv : list) {
            dtos.add(mapToDto(mv));
        }
        return dtos;
    }

    @Override
    public MedicalVisitDto createVisit(MedicalVisitDto dto) {
        return null;
    }

    @Override
    public MedicalVisitDto updateVisit(UUID id, MedicalVisitDto dto) {
        return null;
    }

    @Override
    public void deleteVisit(UUID id) {

    }
}
