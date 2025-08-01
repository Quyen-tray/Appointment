package com.hospital.appointmentservice.receptionist.repository;

import com.hospital.appointmentservice.admin.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoomReceptionRepository extends JpaRepository<Room, UUID> {
    List<Room> findByRoomType(String roomType);
    @Query("SELECT DISTINCT r.roomType FROM Room r WHERE r.roomType IS NOT NULL")
    List<String> findDistinctRoomTypes();
}




