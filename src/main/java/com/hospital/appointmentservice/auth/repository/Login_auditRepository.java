package com.hospital.appointmentservice.auth.repository;

import com.hospital.appointmentservice.auth.model.Login_audit;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface Login_auditRepository extends JpaRepository<Login_audit,Long>, JpaSpecificationExecutor<Login_audit> {
    long countByEventAndTimestampBetween(String loginSuccess, Instant instant, Instant instant1);

    long countByEvent(@Size(max = 50) @NotNull String event);


    @Query("SELECT COUNT(l) FROM Login_audit l WHERE l.event = 'LOGIN_SUCCESS' AND l.timestamp >= :start")
    long countLoginSuccessSince(@Param("start") Instant start);

    @Query("SELECT COUNT(l) FROM Login_audit l WHERE l.event = 'LOGIN_FAILED' AND l.timestamp >= :start")
    long countLoginFailedSince(@Param("start") Instant start);

    @Query("SELECT COUNT(DISTINCT l.username) FROM Login_audit l WHERE l.event = 'FIRST_LOGIN'")
    long countFirstLogins();

    @Query("SELECT l.username, MAX(l.timestamp) FROM Login_audit l WHERE l.event = 'LOGIN_SUCCESS' GROUP BY l.username ORDER BY MAX(l.timestamp) DESC")
    List<Object[]> findRecentLogins(Pageable pageable);

    @Query("SELECT CAST(a.timestamp AS date) AS day, COUNT(a) FROM Login_audit a GROUP BY CAST(a.timestamp AS date)")
    List<Object[]> countLoginsByDay();

    @Query("SELECT COUNT(a) FROM Login_audit a WHERE a.timestamp >= CURRENT_DATE")
    long countTodayLogins();


}
