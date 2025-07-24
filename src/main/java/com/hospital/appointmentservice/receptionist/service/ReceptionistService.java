package com.hospital.appointmentservice.receptionist.service;

import com.hospital.appointmentservice.admin.model.Receptionist;
import com.hospital.appointmentservice.admin.model.Staff;
import com.hospital.appointmentservice.auth.model.UserAccount;
import com.hospital.appointmentservice.auth.repository.UserAccountRepository;
import com.hospital.appointmentservice.patient.dto.PatientProfileDto;
import com.hospital.appointmentservice.patient.dto.UpdateProfileRequestDto;
import com.hospital.appointmentservice.receptionist.dto.PatientResponseDTO;
import com.hospital.appointmentservice.receptionist.dto.PatientDetailDTO;
import com.hospital.appointmentservice.receptionist.dto.PatientHistoryDTO;
import com.hospital.appointmentservice.admin.model.Appointment;
import com.hospital.appointmentservice.receptionist.dto.ReceptionistProfileDTO;
import com.hospital.appointmentservice.receptionist.entity.MedicalRecord;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.repository.PatientRepository;

import com.hospital.appointmentservice.receptionist.repository.ReceptionistRepository;
import com.hospital.appointmentservice.receptionist.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.*;

@Service
public class ReceptionistService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private ReceptionistRepository receptionistRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(this::mapToPatientResponseDTO).collect(Collectors.toList());
    }

    public Page<PatientResponseDTO> getPatientsPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Patient> patientPage = patientRepository.findAll(pageable);

        List<PatientResponseDTO> dtos = patientPage.getContent()
                .stream()
                .map(this::mapToPatientResponseDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, patientPage.getTotalElements());
    }

    public List<PatientResponseDTO> getPatientsByGender(String gender) {
        List<Patient> patients = patientRepository.findAllByGender(gender);
        return patients.stream().map(this::mapToPatientResponseDTO).collect(Collectors.toList());
    }

    public List<PatientResponseDTO> getPatientsByStatus(String status) {
        List<Patient> patients = patientRepository.findAll();

        List<PatientResponseDTO> dtos = new ArrayList<>();

        for (Patient p : patients) {
            Set<Appointment> appointments = p.getAppointments();
            if (appointments != null && !appointments.isEmpty()) {
                // Lấy appointment gần nhất theo scheduledTime
                Optional<Appointment> latestAppointment = appointments.stream()
                        .filter(a -> a.getScheduledTime() != null)
                        .max(Comparator.comparing(Appointment::getScheduledTime));

                if (latestAppointment.isPresent() &&
                        latestAppointment.get().getStatus() != null &&
                        latestAppointment.get().getStatus().equalsIgnoreCase(status)) {

                    dtos.add(mapToPatientResponseDTO(p));
                }
            }
        }

        return dtos;
    }

    public PatientDetailDTO getPatientWithHistory(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        List<PatientHistoryDTO> history = new ArrayList<>();
        Set<Appointment> appointments = patient.getAppointments();

        if (appointments != null) {
            for (Appointment a : appointments) {
                if (a.getMedicalRecord() != null && "Completed".equalsIgnoreCase(a.getStatus())) {
                    history.add(new PatientHistoryDTO(
                            a.getScheduledTime() != null ? a.getScheduledTime().toString() : null,
                            a.getReason(),
                            a.getStatus(),
                            a.getMedicalRecord().getDiagnosis(),
                            a.getMedicalRecord().getNotes(),
                            (a.getMedicalRecord().getCreatedAt() != null) ? a.getMedicalRecord().getCreatedAt().toString() : null
                    ));
                }
            }
        }

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

    public PatientResponseDTO getPatientById(UUID id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        return patient != null ? mapToPatientResponseDTO(patient) : null;
    }

    private PatientResponseDTO mapToPatientResponseDTO(Patient p) {
        List<PatientHistoryDTO> history = new ArrayList<>();
        Set<Appointment> appointments = p.getAppointments();

        String latestStatus = null;

        if (appointments != null && !appointments.isEmpty()) {
            // Sắp xếp theo scheduledTime gần nhất
            Optional<Appointment> latestAppointment = appointments.stream()
                    .filter(a -> a.getScheduledTime() != null)
                    .max(Comparator.comparing(Appointment::getScheduledTime));

            if (latestAppointment.isPresent()) {
                latestStatus = latestAppointment.get().getStatus();
            }

            for (Appointment a : appointments) {
                MedicalRecord mr = a.getMedicalRecord();
                if (mr != null && "Completed".equalsIgnoreCase(a.getStatus())) {
                    history.add(new PatientHistoryDTO(
                            a.getScheduledTime() != null ? a.getScheduledTime().toString() : null,
                            a.getReason(),
                            a.getStatus(),
                            mr.getDiagnosis(),
                            mr.getNotes(),
                            (mr.getCreatedAt() != null) ? mr.getCreatedAt().toString() : null
                    ));
                }
            }

        }

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

    public ReceptionistProfileDTO getProfileByUserName(String username){
        UserAccount user =  userAccountRepository.findUserAccountByUsername(username);
        Staff staff = staffRepository.findByUserAccount(user);
        if(staff == null ){
            throw new RuntimeException("Không tìm thấy nhân viên!");
        }

        ReceptionistProfileDTO dto = new ReceptionistProfileDTO();
        dto.setFullName(staff.getFullName());
        dto.setEmail(staff.getEmail());
        dto.setPhone(staff.getPhone());
        dto.setGender(staff.getGender());
        dto.setDob(staff.getDob());
        return dto ;
    }

    public void updateProfile(String username , UpdateProfileRequestDto dto){
        UserAccount user =  userAccountRepository.findUserAccountByUsername(username);
        Staff staff = staffRepository.findByUserAccount(user);
        Receptionist receptionist = staff.getReceptionist();
        if(receptionist == null){
            throw new RuntimeException("Không tìm thấy nhân viên nào");
        }
        if(dto.getName() != null){
            staff.setFullName(dto.getName());
        }
        if(dto.getEmail() != null){
            staff.setEmail(dto.getEmail());
        }
        if(dto.getGender() != null ){
            staff.setGender(dto.getGender());
        }
        if(dto.getDob() != null){
            staff.setDob(dto.getDob());
        }


        if(dto.getNewPassword() != null && !dto.getOldPassword().isEmpty()){
            if(dto.getOldPassword() == null || dto.getOldPassword().isEmpty()){
                throw new RuntimeException("Vui lòng nhập mật khẩu mới!");
            }

            //check oldpassword
            if(!passwordEncoder.matches(dto.getOldPassword(), staff.getUserAccount().getPasswordHash())){
                throw new RuntimeException("Mật khẩu cũ không đúng!");
            }

            //encode new pass
            String newEncoded = passwordEncoder.encode(dto.getNewPassword());
            staff.getUserAccount().setPasswordHash(newEncoded);
        }

        //save change
        staffRepository.save(staff);
    }
}
