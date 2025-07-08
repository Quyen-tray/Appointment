package com.hospital.appointmentservice.receptionist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class RoomResponseDTO {
    private UUID id;
    private String roomNumber;
    private String roomName;
    private String roomType;
    private Integer floor;
    private String status;
    private String description;
}
