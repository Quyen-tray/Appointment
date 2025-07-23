package com.hospital.appointmentservice.receptionist.service;

import com.hospital.appointmentservice.receptionist.dto.PatientResponseDTO;
import com.hospital.appointmentservice.receptionist.dto.PatientDetailDTO;
import com.hospital.appointmentservice.receptionist.dto.PatientHistoryDTO;
import com.hospital.appointmentservice.admin.model.Appointment;
import com.hospital.appointmentservice.patient.entity.MedicalVisit;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.repository.MedicalVisitRepository;
import com.hospital.appointmentservice.patient.repository.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReceptionistPatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedicalVisitRepository medicalVisitRepository;

    // Lấy tất cả bệnh nhân kèm thông tin cơ bản + lịch sử khám
    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(this::mapToPatientResponseDTO)
                .collect(Collectors.toList());
    }

    // Lấy danh sách phân trang
    public Page<PatientResponseDTO> getPatientsPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Patient> patientPage = patientRepository.findAll(pageable);

        List<PatientResponseDTO> dtos = patientPage.getContent()
                .stream()
                .map(this::mapToPatientResponseDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, patientPage.getTotalElements());
    }

    // Lọc bệnh nhân theo giới tính
    public List<PatientResponseDTO> getPatientsByGender(String gender) {
        List<Patient> patients = patientRepository.findAllByGender(gender);
        return patients.stream()
                .map(this::mapToPatientResponseDTO)
                .collect(Collectors.toList());
    }

    // Lọc bệnh nhân có lần hẹn gần nhất có status cụ thể (status lấy từ Appointment)
    public List<PatientResponseDTO> getPatientsByStatus(String status) {
        List<Patient> patients = patientRepository.findAll();
        List<PatientResponseDTO> dtos = new ArrayList<>();

        for (Patient p : patients) {
            List<MedicalVisit> visits = medicalVisitRepository.findByPatientId(p.getId());

            Optional<Appointment> latestAppointment = visits.stream()
                    .map(MedicalVisit::getAppointment)
                    .filter(Objects::nonNull)
                    .filter(a -> a.getScheduledTime() != null)
                    .max(Comparator.comparing(Appointment::getScheduledTime));

            if (latestAppointment.isPresent() &&
                    status.equalsIgnoreCase(latestAppointment.get().getStatus())) {
                dtos.add(mapToPatientResponseDTO(p));
            }
        }

        return dtos;
    }

    // Lấy chi tiết bệnh nhân cùng lịch sử khám
    public PatientDetailDTO getPatientWithHistory(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        List<MedicalVisit> visits = medicalVisitRepository.findByPatientId(patient.getId());

        List<PatientHistoryDTO> history = visits.stream()
                .map(v -> new PatientHistoryDTO(
                        v.getAppointment() != null && v.getAppointment().getScheduledTime() != null
                                ? v.getAppointment().getScheduledTime().toString() : null,
                        v.getAppointment() != null ? v.getAppointment().getReason() : null,
                        v.getStatus(), // status lấy từ bảng MedicalVisit
                        v.getDiagnosis(),
                        v.getNote(),
                        v.getCreatedAt() != null ? v.getCreatedAt().toString() : null
                ))
                .collect(Collectors.toList());

        return new PatientDetailDTO(
                patient.getId(),
                patient.getFullName(),
                patient.getEmail(),
                patient.getPhone(),
                patient.getDob(),
                patient.getGender(),
                patient.getAddress(),
                history
        );
    }

    // Tìm theo ID
    public PatientResponseDTO getPatientById(UUID id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        return patient != null ? mapToPatientResponseDTO(patient) : null;
    }

    // Chuyển từ entity sang DTO, kèm lịch sử khám và trạng thái lần hẹn gần nhất
    private PatientResponseDTO mapToPatientResponseDTO(Patient p) {
        List<MedicalVisit> visits = medicalVisitRepository.findByPatientId(p.getId());

        // latestStatus lấy từ bảng Appointment
        String latestStatus = visits.stream()
                .map(MedicalVisit::getAppointment)
                .filter(Objects::nonNull)
                .filter(a -> a.getScheduledTime() != null)
                .max(Comparator.comparing(Appointment::getScheduledTime))
                .map(Appointment::getStatus)
                .orElse(null);

        // history[].status lấy từ bảng MedicalVisit
        List<PatientHistoryDTO> history = visits.stream()
                .map(v -> new PatientHistoryDTO(
                        v.getAppointment() != null && v.getAppointment().getScheduledTime() != null
                                ? v.getAppointment().getScheduledTime().toString() : null,
                        v.getAppointment() != null ? v.getAppointment().getReason() : null,
                        v.getStatus(), // status của từng lần khám lấy từ bảng MedicalVisit
                        v.getDiagnosis(),
                        v.getNote(),
                        v.getCreatedAt() != null ? v.getCreatedAt().toString() : null
                ))
                .collect(Collectors.toList());

        return new PatientResponseDTO(
                p.getId(),
                p.getFullName(),
                p.getEmail(),
                p.getPhone(),
                p.getDob(),
                p.getGender(),
                p.getAddress(),
                history,
                latestStatus
        );
    }
}
