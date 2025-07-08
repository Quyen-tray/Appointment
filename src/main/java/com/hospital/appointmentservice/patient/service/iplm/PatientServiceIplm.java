package com.hospital.appointmentservice.patient.service.iplm;

import com.hospital.appointmentservice.auth.service.AuthService;
import com.hospital.appointmentservice.patient.dto.PatientDto;
import com.hospital.appointmentservice.patient.dto.PatientProfileDto;
import com.hospital.appointmentservice.patient.dto.UpdateProfileRequestDto;

import com.hospital.appointmentservice.patient.dto.MedicalVisitDto;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.entity.MedicalVisit;
import com.hospital.appointmentservice.patient.repository.PatientRepository;
import com.hospital.appointmentservice.patient.repository.MedicalVisitRepository;
import com.hospital.appointmentservice.patient.service.PatientService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceIplm implements PatientService {

    private final PasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;
    private final MedicalVisitRepository medicalVisitRepository;
    private final AuthService authService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;


    public PatientServiceIplm(PatientRepository patientRepository,
                              MedicalVisitRepository medicalVisitRepository,
                              AuthService authService, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.medicalVisitRepository = medicalVisitRepository;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<PatientDto> getPatients() {
        return patientRepository.findAll().stream()
                .map(patient -> {
                    PatientDto dto = new PatientDto();
                    dto.setId(patient.getId().toString());
                    dto.setUserId(patient.getUser() != null
                            ? patient.getUser().getId().toString()
                            : null);
                    dto.setFullName(patient.getFullName());
                    dto.setDob(patient.getDob() != null ? patient.getDob().toString() : null);
                    dto.setGender(patient.getGender());
                    dto.setPhone(patient.getPhone());
                    dto.setEmail(patient.getEmail());
                    dto.setInsuranceId(patient.getInsuranceId());
                    dto.setMedicalVisits(Collections.emptyList());
                    return dto;
                }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PatientDto getPatientById(UUID id) {
        return patientRepository.findWithVisitsById(id)
                .map(patient -> {
                    PatientDto dto = new PatientDto();
                    dto.setId(patient.getId().toString());
                    dto.setUserId(patient.getUser() != null ? patient.getUser().getId().toString() : null);
                    dto.setFullName(patient.getFullName());
                    dto.setDob(patient.getDob() != null ? patient.getDob().toString() : null);
                    dto.setGender(patient.getGender());
                    dto.setPhone(patient.getPhone());
                    dto.setEmail(patient.getEmail());
                    dto.setInsuranceId(patient.getInsuranceId());

                    // Map medicalVisits
                    List<MedicalVisitDto> visits = new ArrayList<>();
                    if (patient.getMedicalVisits() != null) {
                        for (MedicalVisit mv : patient.getMedicalVisits()) {
                            MedicalVisitDto mvDto = new MedicalVisitDto();
                            mvDto.setId(mv.getId().toString());
                            if (mv.getAppointment() != null && mv.getAppointment().getId() != null) {
                                mvDto.setAppointmentId(mv.getAppointment().getId().toString());
                            }
                            if (mv.getDoctor() != null && mv.getDoctor().getId() != null) {
                                mvDto.setDoctorId(mv.getDoctor().getId().toString());
                            }
                            mvDto.setDiagnosis(mv.getDiagnosis());
                            mvDto.setNote(mv.getNote());
                            if (mv.getCreatedAt() != null) {
                                mvDto.setCreatedAt(mv.getCreatedAt().format(dateFormatter));
                            }
                            visits.add(mvDto);
                        }
                    }
                    dto.setMedicalVisits(visits);
                    return dto;
                })
                .orElse(null);
    }

    @Override
    public Patient addPatient(PatientDto patientDto) {
        Patient patient = new Patient();
        patient.setUser(authService.findByUserAccount(patientDto.getFullName()));
        patient.setEmail(patientDto.getEmail());
        return patientRepository.save(patient);
    }

    @Override
    public Boolean existsPatientByEmail(String email) {
        return patientRepository.existsPatientByEmail(email);
    }

    public PatientProfileDto getProfileByUserName(String username){
        Patient patient = patientRepository.findByUser_Username(username);
        if(patient == null ){
            throw new RuntimeException("Không tìm thấy bệnh nhân!");
        }

        PatientProfileDto dto = new PatientProfileDto();
        dto.setFullName(patient.getFullName());
        dto.setEmail(patient.getEmail());
        dto.setPhone(patient.getPhone());
        dto.setGender(patient.getGender());
        dto.setDob(patient.getDob());
        return dto ;
    }

    @Override
    public void updateProfile(String username , UpdateProfileRequestDto dto){
        Patient patient = patientRepository.findByUser_Username(username);
        if(patient == null){
            throw new RuntimeException("Không tìm thấy bệnh nhân!");
        }
        if(dto.getName() != null){
            patient.setFullName(dto.getName());
        }
        if(dto.getEmail() != null){
            patient.setEmail(dto.getEmail());
        }
        if(dto.getGender() != null ){
            patient.setGender(dto.getGender());
        }
        if(dto.getDob() != null){
            patient.setDob(dto.getDob());
        }
        if(dto.getAvatar() != null){
            patient.setAvatar(dto.getAvatar());
        }

        if(dto.getNewPassword() != null && !dto.getOldPassword().isEmpty()){
            if(dto.getOldPassword() == null || dto.getOldPassword().isEmpty()){
                throw new RuntimeException("Vui lòng nhập mật khẩu mới!");
            }

            //check oldpassword
            if(!passwordEncoder.matches(dto.getOldPassword(), patient.getUser().getPasswordHash())){
                throw new RuntimeException("Mật khẩu cũ không đúng!");
            }

            //encode new pass
            String newEncoded = passwordEncoder.encode(dto.getNewPassword());
            patient.getUser().setPasswordHash(newEncoded);
        }

        //save change 
        patientRepository.save(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalVisitDto> getMedicalVisitsByPatientId(UUID patientId) {
        if (!patientRepository.existsById(patientId)) {
            return null;
        }
        List<MedicalVisit> visits = medicalVisitRepository.findByPatientId(patientId);
        List<MedicalVisitDto> dtos = new ArrayList<>();
        for (MedicalVisit mv : visits) {
            MedicalVisitDto mvDto = new MedicalVisitDto();
            mvDto.setId(mv.getId().toString());
            if (mv.getAppointment() != null && mv.getAppointment().getId() != null) {
                mvDto.setAppointmentId(mv.getAppointment().getId().toString());
            }
            if (mv.getDoctor() != null && mv.getDoctor().getId() != null) {
                mvDto.setDoctorId(mv.getDoctor().getId().toString());
            }
            mvDto.setDiagnosis(mv.getDiagnosis());
            mvDto.setNote(mv.getNote());
            if (mv.getCreatedAt() != null) {
                mvDto.setCreatedAt(mv.getCreatedAt().format(dateFormatter));
            }
            dtos.add(mvDto);
        }
        return dtos;
    }
}
