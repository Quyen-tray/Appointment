package com.hospital.appointmentservice.receptionist.service;

import com.hospital.appointmentservice.receptionist.dto.RoomResponseDTO;
import com.hospital.appointmentservice.receptionist.dto.RoomUpdateRequestDTO;

import java.util.List;
import java.util.UUID;

public interface ReceptionistRoomService {
    List<RoomResponseDTO> getAllRooms();
    void updateRoom(UUID id, RoomUpdateRequestDTO requestDTO);
}
