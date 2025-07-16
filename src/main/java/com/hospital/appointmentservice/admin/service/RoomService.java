package com.hospital.appointmentservice.admin.service;

import com.hospital.appointmentservice.admin.dto.RoomDto;
import com.hospital.appointmentservice.admin.model.Room;

import java.util.List;

public interface RoomService {
    List<RoomDto> getAllRooms();
}
