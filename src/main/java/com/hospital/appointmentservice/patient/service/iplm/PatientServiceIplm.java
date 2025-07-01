package com.hospital.appointmentservice.patient.service.iplm;

import com.hospital.appointmentservice.auth.service.AuthService;
import com.hospital.appointmentservice.patient.dto.PatientDto;
import com.hospital.appointmentservice.patient.dto.PatientProfileDto;
import com.hospital.appointmentservice.patient.dto.UpdateProfileRequestDto;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.repository.PatientRepository;
import com.hospital.appointmentservice.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceIplm implements PatientService {

    private final PasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;
    private final AuthService authService;

    @Autowired
    public PatientServiceIplm(PatientRepository patientRepository, AuthService authService, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<PatientDto> getPatients() {

        return List.of();
    }

    @Override
    public PatientDto getPatientById(UUID id) {
        return null;
    }
//This method used add patient when register account so don't used add patient if not register account
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

}
