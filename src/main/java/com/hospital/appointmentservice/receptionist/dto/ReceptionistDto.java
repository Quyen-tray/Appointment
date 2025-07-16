package com.hospital.appointmentservice.receptionist.dto;

import com.hospital.appointmentservice.admin.model.Appointment;
import com.hospital.appointmentservice.admin.model.Staff;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceptionistDto {
    private UUID id;
    private String fullName;
    private String note;
}
