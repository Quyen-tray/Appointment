package com.hospital.appointmentservice.auth.repository;

import com.hospital.appointmentservice.auth.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.hospital.appointmentservice.auth.dto.UserAccountDto;

import java.util.UUID;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, UUID>, JpaSpecificationExecutor<UserAccount> {
    boolean existsUserAccountByUsername(String username);

    UserAccount findUserAccountByUsername(String username);
}
