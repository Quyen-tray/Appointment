package com.hospital.appointmentservice.auth.service;

import com.hospital.appointmentservice.auth.dto.UserAccountDto;
import com.hospital.appointmentservice.auth.model.UserAccount;

import java.util.UUID;

public interface UserAccountService {
    public void registerUser(UserAccountDto userAccountDto);
    public UserAccount findByUserAccount(String username);
    public Boolean existsByUserName(String username);
    public Boolean authenticateByUserName(String username, String password);
}
