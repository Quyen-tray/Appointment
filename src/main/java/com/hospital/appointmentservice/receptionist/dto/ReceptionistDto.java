package com.hospital.appointmentservice.receptionist.dto;

import lombok.*;
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
