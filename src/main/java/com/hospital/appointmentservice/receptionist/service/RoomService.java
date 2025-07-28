package com.hospital.appointmentservice.receptionist.service;

import com.hospital.appointmentservice.receptionist.dto.RoomDto;
import java.util.List;

public interface RoomService {
    List<RoomDto> getAllRooms();
    List<String> getAllRoomTypes();
}
