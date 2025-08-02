package com.hospital.appointmentservice.receptionist.controller;

import com.hospital.appointmentservice.receptionist.dto.RoomResponseDTO;
import com.hospital.appointmentservice.receptionist.dto.RoomUpdateRequestDTO;
import com.hospital.appointmentservice.receptionist.service.ReceptionistRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
public class ReceptionistRoomController {
    private final ReceptionistRoomService roomService;

    public ReceptionistRoomController(ReceptionistRoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<RoomResponseDTO>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateRoom(
            @PathVariable UUID id,
            @RequestBody RoomUpdateRequestDTO requestDTO
    ) {
        roomService.updateRoom(id, requestDTO);
        return ResponseEntity.ok("Phòng đã được cập nhật thành công.");
    }
}
