package com.hospital.appointmentservice.patient.service.iplm;

import com.hospital.appointmentservice.admin.model.Appointment;
import com.hospital.appointmentservice.patient.dto.LabRequestDto;
import com.hospital.appointmentservice.patient.dto.MedicalVisitDto;
import com.hospital.appointmentservice.patient.dto.MedicalVisitResponse;
import com.hospital.appointmentservice.patient.dto.UpdateMedicalNote;
import com.hospital.appointmentservice.patient.entity.LabRequest;
import com.hospital.appointmentservice.patient.entity.MedicalVisit;
import com.hospital.appointmentservice.patient.repository.AppointmentRepository;
import com.hospital.appointmentservice.patient.repository.LabRequestRepository;
import com.hospital.appointmentservice.patient.repository.MedicalVisitRepository;
import com.hospital.appointmentservice.patient.repository.PatientRepository;
import com.hospital.appointmentservice.patient.service.MedicalVisitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicalVisitServiceImpl implements MedicalVisitService {
    private final MedicalVisitRepository medicalVisitRepository;
    private final PatientRepository patientRepository; // để kiểm tra tồn tại patient khi tạo mới
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final LabRequestRepository labRequestRepository;
    private final AppointmentRepository appointmentRepository;

    public MedicalVisitServiceImpl(MedicalVisitRepository medicalVisitRepository,
                                   PatientRepository patientRepository,
                                   LabRequestRepository labRequestRepository, AppointmentRepository appointmentRepository) {
        this.medicalVisitRepository = medicalVisitRepository;
        this.patientRepository = patientRepository;
        this.labRequestRepository = labRequestRepository;
        this.appointmentRepository = appointmentRepository;
    }

    private MedicalVisitDto mapToDto(MedicalVisit mv) {
        MedicalVisitDto dto = new MedicalVisitDto();
        dto.setId(mv.getId().toString());
        if (mv.getAppointment() != null && mv.getAppointment().getId() != null) {
            dto.setAppointmentId(mv.getAppointment().getId().toString());
        }
        if (mv.getDoctor() != null && mv.getDoctor().getId() != null) {
            dto.setDoctorId(mv.getDoctor().getId().toString());
            dto.setDoctorName(mv.getDoctor().getFullName());
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


    private void mapToEntity(MedicalVisit mv, MedicalVisitDto dto) {
        mv.setDiagnosis(dto.getDiagnosis());
        mv.setNote(dto.getNote());
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
    public List<MedicalVisitDto> getVisitsByPatientId(UUID patientId, LocalDate fromDate, LocalDate toDate, int page, int size) {
        if (!patientRepository.existsById(patientId)) {
            return new ArrayList<>();
        }

        // Xử lý khoảng thời gian nếu được cung cấp
        LocalDateTime from = fromDate != null ? fromDate.atStartOfDay() : null;
        LocalDateTime to = toDate != null ? toDate.atTime(23, 59, 59) : null;

        Pageable pageable = PageRequest.of(page, size);
        Page<MedicalVisit> resultPage = medicalVisitRepository.findByPatientIdWithDateFilter(patientId, from, to, pageable);

        List<MedicalVisitDto> dtos = new ArrayList<>();
        for (MedicalVisit mv : resultPage.getContent()) {
            dtos.add(mapToDto(mv));
        }
        return dtos;
    }

    @Override
    public MedicalVisitResponse getByAppointmentAndPatientId(UUID appointmentId, UUID patientId) {
        if (!patientRepository.existsById(patientId)) {
            return null;
        }

        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);

        MedicalVisit medicalVisit = medicalVisitRepository.findByAppointment_Id(appointmentId);
        if (medicalVisit == null) {
            MedicalVisit createMedicalVisit = new MedicalVisit();
            createMedicalVisit.setAppointment(appointment.get());
            createMedicalVisit.setDoctor(appointment.get().getDoctor());
            createMedicalVisit.setPatient(appointment.get().getPatient());
            createMedicalVisit.setStatus("PROGRESS'");
            MedicalVisit newMedical = medicalVisitRepository.save(createMedicalVisit);
            return MedicalVisitResponse.builder()
                    .medicalVisit(mapToDto(newMedical))
                    .build();
        }

        MedicalVisitResponse response = new MedicalVisitResponse();
        response.setMedicalVisit(mapToDto(medicalVisit));
        List<LabRequest> labRequests = labRequestRepository.findByVisit_Id(medicalVisit.getId());
        List<LabRequestDto> labRequestDtos = labRequests.stream()
                .map(lr -> LabRequestDto.builder()
                        .labId(lr.getLabId())
                        .visitId(lr.getVisit() != null ? lr.getVisit().getId() : null)
                        .requestedBy(lr.getRequestedBy())
                        .roomId(lr.getRoomId())
                        .testType(lr.getTestType())
                        .result(lr.getResult())
                        .status(lr.getStatus())
                        .price(lr.getPrice())
                        .build())
                .collect(Collectors.toList());

        response.setLabRequests(labRequestDtos);
        return response;
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

    @Override
    public void updateNote(UUID appointmentId, UpdateMedicalNote update) {
        MedicalVisit medicalVisit = medicalVisitRepository.findByAppointment_Id(appointmentId);
        medicalVisit.setNote(update.getNote());
        medicalVisitRepository.save(medicalVisit);
    }
}
