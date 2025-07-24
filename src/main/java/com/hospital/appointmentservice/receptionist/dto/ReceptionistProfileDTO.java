package com.hospital.appointmentservice.receptionist.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceptionistProfileDTO {
    private UUID id;
    private String fullName;
    private String note;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    private String phone ;
    private String email;
    private String  gender ;
}
