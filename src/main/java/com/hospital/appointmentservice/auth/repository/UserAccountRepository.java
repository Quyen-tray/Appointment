package com.hospital.appointmentservice.auth.repository;

import com.hospital.appointmentservice.auth.model.UserAccount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, UUID>, JpaSpecificationExecutor<UserAccount> {
    boolean existsUserAccountByUsername(String username);

    @EntityGraph(attributePaths = "staff")
    UserAccount findUserAccountByUsername(String username);
}
