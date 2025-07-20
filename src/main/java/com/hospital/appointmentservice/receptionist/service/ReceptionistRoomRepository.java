package com.hospital.appointmentservice.admin.repository;

import com.hospital.appointmentservice.admin.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReceptionistRoomRepository extends JpaRepository<Room, UUID> {

    List<Room> findByRoomType(String roomType);
}