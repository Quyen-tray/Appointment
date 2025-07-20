package com.hospital.appointmentservice.receptionist.service;

import com.hospital.appointmentservice.receptionist.dto.RoomDto;
import com.hospital.appointmentservice.admin.model.Room;
import com.hospital.appointmentservice.receptionist.repository.RoomReceptionRepository;
import com.hospital.appointmentservice.receptionist.service.impl.RoomService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomServiceImpl implements RoomService {
    RoomReceptionRepository roomRepository;


    @Override
    public List<RoomDto> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();

        return rooms.stream()
                .map(room -> {
                    RoomDto dto = new RoomDto();
                    dto.setId(room.getId());
                    dto.setRoomName(room.getRoomName());
                    dto.setRoomType(room.getRoomType());
                    return dto;
                })
                .collect(Collectors.toList());

    }
}
