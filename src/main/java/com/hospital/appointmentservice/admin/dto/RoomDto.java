package com.hospital.appointmentservice.admin.dto;

import com.hospital.appointmentservice.admin.model.Department;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;

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
