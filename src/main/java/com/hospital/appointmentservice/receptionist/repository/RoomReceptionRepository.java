package com.hospital.appointmentservice.receptionist.repository;

import com.hospital.appointmentservice.admin.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoomReceptionRepository extends JpaRepository<Room, UUID> {
}




