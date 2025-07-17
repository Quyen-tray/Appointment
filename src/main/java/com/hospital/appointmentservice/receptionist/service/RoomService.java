package com.hospital.appointmentservice.receptionist.service;

import com.hospital.appointmentservice.admin.repository.RoomRepository;
import com.hospital.appointmentservice.receptionist.dto.RoomResponseDTO;
import com.hospital.appointmentservice.admin.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoomService {
    // test push lên GitHub
    @Autowired
    private RoomRepository roomRepository;

    public List<RoomResponseDTO> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<RoomResponseDTO> getRoomById(UUID id) {
        return roomRepository.findById(id).map(this::convertToDTO);
    }



    private RoomResponseDTO convertToDTO(Room room) {
        return new RoomResponseDTO(
                room.getId(),
                room.getRoomNumber(),
                room.getRoomName(),
                room.getRoomType(),
                room.getFloor(),
                room.getStatus(),
                room.getDescription()
        );
    }
}
