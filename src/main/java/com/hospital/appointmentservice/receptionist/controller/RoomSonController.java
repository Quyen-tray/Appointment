package com.hospital.appointmentservice.receptionist.controller;

import com.hospital.appointmentservice.receptionist.dto.RoomDto;
import com.hospital.appointmentservice.receptionist.service.impl.RoomService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/room")
// Annotation gen constructor
@RequiredArgsConstructor
// Annotation make field private and final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomSonController {
    RoomService roomService;

    @GetMapping
    public ResponseEntity<?> getAllRooms() {
        try {
            List<RoomDto> rooms = roomService.getAllRooms();

            return ResponseEntity.status(HttpStatus.OK).body(rooms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errors in get rooms process" + e.getMessage());
        }
    }
}
