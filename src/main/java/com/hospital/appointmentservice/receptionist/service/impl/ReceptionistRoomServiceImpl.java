package com.hospital.appointmentservice.receptionist.service.impl;

import com.hospital.appointmentservice.admin.model.Room;
import com.hospital.appointmentservice.receptionist.dto.RoomResponseDTO;
import com.hospital.appointmentservice.receptionist.dto.RoomUpdateRequestDTO;
import com.hospital.appointmentservice.receptionist.repository.RoomReceptionRepository;
import com.hospital.appointmentservice.receptionist.service.ReceptionistRoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReceptionistRoomServiceImpl implements ReceptionistRoomService {
    private final RoomReceptionRepository roomRepository;
    public ReceptionistRoomServiceImpl(RoomReceptionRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public List<RoomResponseDTO> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(room -> new RoomResponseDTO(
                        room.getId(),
                        room.getRoomNumber(),
                        room.getRoomName(),
                        room.getRoomType(),
                        room.getFloor(),
                        room.getStatus(),
                        room.getDescription()
                ))
                .collect(Collectors.toList());
    }
    @Override
    public void updateRoom(UUID id, RoomUpdateRequestDTO requestDTO) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng với ID: " + id));
        room.setRoomNumber(requestDTO.getRoomNumber());
        room.setRoomName(requestDTO.getRoomName());
        room.setRoomType(requestDTO.getRoomType());
        room.setFloor(requestDTO.getFloor());
        room.setStatus(requestDTO.getStatus());
        room.setDescription(requestDTO.getDescription());

        roomRepository.save(room);
    }
}
