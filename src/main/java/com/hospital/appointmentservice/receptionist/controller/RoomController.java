package com.hospital.appointmentservice.receptionist.controller;

import com.hospital.appointmentservice.receptionist.dto.RoomResponseDTO;
import com.hospital.appointmentservice.receptionist.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "http://localhost:3000")
public class RoomController {

    @Autowired
    private RoomService roomService;


    @GetMapping
    public ResponseEntity<List<RoomResponseDTO>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }


    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDTO> getRoomById(@PathVariable UUID id) {
        return roomService.getRoomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //http://localhost:8081/api/rooms/filter?type=tenphongkham
    @GetMapping("/filter")
    public ResponseEntity<List<RoomResponseDTO>> getRoomsByType(@RequestParam String type) {
        return ResponseEntity.ok(roomService.getRoomsByType(type));
    }

    // http://localhost:8081/api/rooms/paginated?page=0&size=5
    @GetMapping("/paginated")
    public ResponseEntity<Page<RoomResponseDTO>> getRoomsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<RoomResponseDTO> pagedRooms = roomService.getRoomsPaginated(pageable);
        return ResponseEntity.ok(pagedRooms);
    }


}
