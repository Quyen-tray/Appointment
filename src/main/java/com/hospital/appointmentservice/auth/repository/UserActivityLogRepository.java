package com.hospital.appointmentservice.auth.repository;

import com.hospital.appointmentservice.auth.model.UserActivityLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long>, JpaSpecificationExecutor<UserActivityLog> {
    @Query("SELECT u.endpoint, COUNT(u) FROM UserActivityLog u GROUP BY u.endpoint ORDER BY COUNT(u) DESC")
    List<Object[]> findTopEndpoints(Pageable pageable);

    @Query(
            value = "SELECT CAST([timestamp] AS DATE) AS day, COUNT(*) " +
                    "FROM user_activity_log " +
                    "GROUP BY CAST([timestamp] AS DATE) " +
                    "ORDER BY day",
            nativeQuery = true
    )
    List<Object[]> countApiCallsByDay();

    @Query("SELECT u.endpoint, AVG(u.durationMs) FROM UserActivityLog u GROUP BY u.endpoint")
    List<Object[]> averageDurationPerEndpoint();

    @Query("SELECT u.method, COUNT(u) FROM UserActivityLog u GROUP BY u.method")
    List<Object[]> countRequestsByMethod();
}
