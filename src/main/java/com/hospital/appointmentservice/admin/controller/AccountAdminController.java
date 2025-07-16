package com.hospital.appointmentservice.admin.controller;

import com.hospital.appointmentservice.admin.dto.StaffAdminDto;
import com.hospital.appointmentservice.admin.service.ReceptionistAdminService;
import com.hospital.appointmentservice.admin.service.StaffAdminService;
import com.hospital.appointmentservice.auth.dto.UserAccountDto;
import com.hospital.appointmentservice.auth.service.AuthService;
import com.hospital.appointmentservice.patient.dto.PatientDto;
import com.hospital.appointmentservice.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/account")
public class AccountAdminController {
    @Autowired private AuthService authService;
    @Autowired private PatientService patientService;
    @Autowired private StaffAdminService staffAdminService;
    @Autowired private ReceptionistAdminService receptionistAdminService;


    @PostMapping("/create")
    public ResponseEntity<UserAccountDto> createAccount(@RequestBody UserAccountDto dto) {
        try {
            //valid name existed in database table user account
            if(authService.existsByUserName(dto.getUsername())){
                return ResponseEntity.badRequest().body(dto);
            }

            if(dto.getRoles() == null){
                return ResponseEntity.badRequest().body(dto);
            } else if (dto.getRoles().equals("PATIENT")) {
                UserAccountDto user = authService.create(dto);
                //Create 1 record patient in patient table
                PatientDto patientDto = new PatientDto();
                patientDto.setFullName(dto.getUsername());
                patientService.addPatient(patientDto);
                return ResponseEntity.ok(user);
            }else if(dto.getRoles().equals("ADMIN")) {
                UserAccountDto user = authService.create(dto);

                //Create 1 record staff in staff table
                StaffAdminDto staffAdminDto = new StaffAdminDto();
                staffAdminDto.setFullName(dto.getUsername());
                staffAdminService.addStaff(staffAdminDto);
                return ResponseEntity.ok(user);
            }else {
                UserAccountDto user = authService.create(dto);

                //Create 1 record staff in staff table
                StaffAdminDto staffAdminDto = new StaffAdminDto();
                staffAdminDto.setFullName(dto.getUsername());
                staffAdminService.addStaff(staffAdminDto);

                //Create 1 record receptionist
                receptionistAdminService.create(staffAdminService.getStaff(authService.findByUserAccount(dto.getUsername()).getId()));
                return ResponseEntity.ok(user);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Page<UserAccountDto>> listAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(defaultValue = "") String keyword) {
        return ResponseEntity.ok(authService.getAll(page, size, sortBy, direction, keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAccountDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(authService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAccountDto> update(@PathVariable UUID id, @RequestBody UserAccountDto dto) {
        return ResponseEntity.ok(authService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        authService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
