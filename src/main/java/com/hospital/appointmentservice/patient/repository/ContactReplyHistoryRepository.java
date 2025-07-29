package com.hospital.appointmentservice.patient.repository;

import com.hospital.appointmentservice.patient.entity.ContactReplyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ContactReplyHistoryRepository extends JpaRepository<ContactReplyHistory, UUID> {
    List<ContactReplyHistory> findByContactId(UUID contactId);
}
