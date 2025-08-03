package com.hospital.appointmentservice.receptionist.dto;

import lombok.Data;

@Data
public class RoomUpdateRequestDTO {
    private String roomNumber;
    private String roomName;
    private String roomType;
    private Integer floor;
    private String status;
    private String description;
}
