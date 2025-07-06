package com.hospital.appointmentservice.auth.service;

import com.hospital.appointmentservice.auth.dto.UserAccountDto;
import com.hospital.appointmentservice.auth.model.UserAccount;
import com.hospital.appointmentservice.auth.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserAccountService {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    
    public AuthService(UserAccountRepository userAccountRepository,PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void registerUser(UserAccountDto userAccountDto) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(userAccountDto.getUsername());
        //Encryption password according to Database
        String password = passwordEncoder.encode(userAccountDto.getPassword());
        userAccount.setPasswordHash(password);
        //Change status of user account
        String status = "PENDING";
        userAccount.setStatus(status);
        userAccount.setRole(userAccountDto.getRoles());
        //Save user account in database
        userAccountRepository.save(userAccount);
    }

    @Override
    public UserAccount findByUserAccount(String username) {
        return userAccountRepository.findUserAccountByUsername(username);
    }

    @Override
    public Boolean existsByUserName(String username) {
        //check in database existed username ?
        return userAccountRepository.existsUserAccountByUsername(username);
    }

    @Override
    public Boolean authenticateByUserName(String username, String password) {
        return null;
    }

}
