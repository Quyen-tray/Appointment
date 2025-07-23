package com.hospital.appointmentservice.receptionist.dto;

import com.hospital.appointmentservice.admin.model.Department;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private UUID id;
    private String roomName;
    private Department department;
    private String roomType;
}
