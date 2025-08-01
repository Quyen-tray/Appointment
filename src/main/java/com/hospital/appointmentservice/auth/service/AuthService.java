package com.hospital.appointmentservice.auth.service;

import com.hospital.appointmentservice.auth.dto.UserAccountDto;
import com.hospital.appointmentservice.auth.model.UserAccount;
import com.hospital.appointmentservice.auth.repository.UserAccountRepository;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.repository.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService implements UserAccountService {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;

    
    public AuthService(UserAccountRepository userAccountRepository,
                       PasswordEncoder passwordEncoder,
                       PatientRepository patientRepository) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.patientRepository=patientRepository;
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

    private UserAccountDto toDto(UserAccount entity) {
        return new UserAccountDto(
                entity.getId(),
                entity.getUsername(),
                entity.getRole(),
                entity.getStatus(),
                entity.getLastLogin()
        );
    }

    private UserAccount toEntity(UserAccountDto dto) {
        UserAccount user = new UserAccount();
        user.setUsername(dto.getUsername());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRoles());
        user.setStatus(dto.getStatus());
        return user;
    }

    @Override
    public UserAccountDto create(UserAccountDto dto) {
        UserAccount user = toEntity(dto);
        return toDto(userAccountRepository.save(user));
    }

    @Override
    public Page<UserAccountDto> getAll(int page, int size, String sortBy, String direction, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        Specification<UserAccount> spec = (root, query, cb) -> {
            if (keyword != null && !keyword.isEmpty()) {
                String likePattern = "%" + keyword.toLowerCase() + "%";
                return cb.like(cb.lower(root.get("username")), likePattern);
            }
            return cb.conjunction();
        };
        return userAccountRepository.findAll(spec, pageable).map(this::toDto);
    }

    @Override
    public UserAccountDto update(UUID id, UserAccountDto dto) {
        UserAccount existing = userAccountRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        if(!existsByUserName(existing.getUsername())) {
            existing.setUsername(dto.getUsername());
        }
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            existing.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }
        existing.setRole(dto.getRoles());
        existing.setStatus(dto.getStatus());
        return toDto(userAccountRepository.save(existing));
    }

    @Override
    public void delete(UUID id) {
        userAccountRepository.findById(id).ifPresent(userAccount -> {
            userAccount.setStatus("INACTIVE");
            userAccountRepository.save(userAccount);
        });
    }

    @Override
    public UserAccountDto getById(UUID id) {
        return userAccountRepository.findById(id).map(this::toDto).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override
    public UserAccount getByEmail(String email) throws RuntimeException {
        Patient patient = patientRepository.findByEmail(email);
        return patient.getUser();
    }
}
