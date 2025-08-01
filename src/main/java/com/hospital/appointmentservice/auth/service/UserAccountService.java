package com.hospital.appointmentservice.auth.service;

import com.hospital.appointmentservice.auth.dto.UserAccountDto;
import com.hospital.appointmentservice.auth.model.UserAccount;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface UserAccountService {
    public void registerUser(UserAccountDto userAccountDto);
    public UserAccount findByUserAccount(String username);
    public Boolean existsByUserName(String username);
    public Boolean authenticateByUserName(String username, String password);

    UserAccountDto create(UserAccountDto dto);
    Page<UserAccountDto> getAll(int page, int size, String sortBy, String direction, String keyword);
    UserAccountDto update(UUID id, UserAccountDto dto);
    void delete(UUID id);
    UserAccountDto getById(UUID id);
    UserAccount getByEmail(String email);
}
