package com.hospital.appointmentservice.receptionist.service;

import com.hospital.appointmentservice.receptionist.dto.RoomResponseDTO;
import com.hospital.appointmentservice.admin.model.Room;
import com.hospital.appointmentservice.receptionist.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


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

    public List<RoomResponseDTO> getRoomsByType(String roomType) {
        List<Room> rooms = roomRepository.findByRoomType(roomType);
        return rooms.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Page<RoomResponseDTO> getRoomsPaginated(Pageable pageable) {
        return roomRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

}
