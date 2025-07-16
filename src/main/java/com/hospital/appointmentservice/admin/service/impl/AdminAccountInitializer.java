package com.hospital.appointmentservice.admin.service.impl;

import com.hospital.appointmentservice.admin.dto.StaffAdminDto;
import com.hospital.appointmentservice.admin.service.StaffAdminService;
import com.hospital.appointmentservice.auth.model.UserAccount;
import com.hospital.appointmentservice.auth.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminAccountInitializer implements ApplicationRunner {
    @Autowired private UserAccountRepository userAccountRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private StaffAdminService staffAdminService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        String defaultUsername = "admin";
        if (!userAccountRepository.existsUserAccountByUsername(defaultUsername)) {
            UserAccount admin = new UserAccount();
            admin.setUsername(defaultUsername);
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            admin.setStatus("ACTIVE");
            admin.setLastLogin(null);
            userAccountRepository.save(admin);

            //Create 1 record staff in staff table
            StaffAdminDto staffAdminDto = new StaffAdminDto();
            staffAdminDto.setFullName(defaultUsername);
            staffAdminService.addStaff(staffAdminDto);
        }
    }
}
