package com.hospital.appointmentservice.receptionist.service.impl;

import com.hospital.appointmentservice.admin.model.Receptionist;
import com.hospital.appointmentservice.admin.model.Staff;
import com.hospital.appointmentservice.auth.model.UserAccount;
import com.hospital.appointmentservice.auth.repository.UserAccountRepository;
import com.hospital.appointmentservice.patient.dto.UpdateProfileRequestDto;
import com.hospital.appointmentservice.receptionist.dto.PatientResponseDTO;
import com.hospital.appointmentservice.receptionist.dto.PatientDetailDTO;
import com.hospital.appointmentservice.receptionist.dto.PatientHistoryDTO;
import com.hospital.appointmentservice.admin.model.Appointment;
import com.hospital.appointmentservice.patient.entity.MedicalVisit;
import com.hospital.appointmentservice.receptionist.dto.ReceptionistProfileDTO;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.repository.MedicalVisitRepository;
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
public class ReceptionistServiceImp {

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


        //save change
        staffRepository.save(staff);
    }
}
