package com.hospital.appointmentservice.receptionist.service.impl;

import com.hospital.appointmentservice.auth.model.UserAccount;
import com.hospital.appointmentservice.auth.repository.UserAccountRepository;
import com.hospital.appointmentservice.patient.entity.MedicalVisit;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.repository.PatientRepository;
import com.hospital.appointmentservice.receptionist.dto.PatientDetailDTO;
import com.hospital.appointmentservice.receptionist.dto.PatientHistoryDTO;
import com.hospital.appointmentservice.receptionist.dto.PatientRegisterRequest;
import com.hospital.appointmentservice.receptionist.repository.MedicalVisitRepRepository;
import com.hospital.appointmentservice.receptionist.service.ReceptionistPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReceptionistPatientServiceImp implements ReceptionistPatientService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private MedicalVisitRepRepository medicalVisitRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public List<PatientDetailDTO> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(p -> new PatientDetailDTO(
                        p.getId(),
                        p.getFullName(),
                        p.getEmail(),
                        p.getPhone(),
                        p.getDob(),
                        p.getGender(),
                        p.getAddress()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientHistoryDTO> getPatientHistory(UUID patientId) {
        List<MedicalVisit> visits = medicalVisitRepository.findByPatientId(patientId);
        return visits.stream().map(v -> new PatientHistoryDTO(
                v.getStatus(),
                v.getDiagnosis(),
                v.getNote(),
                v.getCreatedAt().toString()
        )).collect(Collectors.toList());
    }
    @Override
    public Patient getPatientById(UUID id) {
        return patientRepository.findById(id).orElse(null);
    }

    @Override
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public void addNewPatient(PatientRegisterRequest request) {
        // Kiểm tra trùng username
        if (userAccountRepository.existsUserAccountByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại!");
        }

        // Tạo user
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(request.getUsername());
        userAccount.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        userAccount.setRole("PATIENT");
        userAccount.setStatus("ACTIVE");

        userAccountRepository.save(userAccount);
        // Tạo patient
        Patient patient = new Patient();
        patient.setFullName(request.getFullName());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        patient.setDob(LocalDate.parse(request.getDob())); // nhớ định dạng yyyy-MM-dd
        patient.setGender(request.getGender());
        patient.setAddress(request.getAddress());
        patient.setUser(userAccount);  // liên kết với tài khoản

        patientRepository.save(patient);
    }
}

