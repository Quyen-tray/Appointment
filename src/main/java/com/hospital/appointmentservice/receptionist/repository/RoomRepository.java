package com.hospital.appointmentservice.receptionist.repository;

import com.hospital.appointmentservice.admin.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {
    List<Room> findByRoomType(String roomType);
}
