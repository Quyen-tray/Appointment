package com.hospital.appointmentservice.receptionist.dto;

import com.hospital.appointmentservice.auth.dto.UserAccountDto;
import com.hospital.appointmentservice.doctor.dto.DoctorDetailDto;
import com.hospital.appointmentservice.patient.dto.PatientDto;
import com.hospital.appointmentservice.patient.dto.RelativeResponseDto;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private UUID id;
    private PatientDto patient;
    private DoctorDetailDto doctor;
    private RoomDto room;
    private LocalDateTime scheduledTime;
    private String status;
    private UserAccountDto createdBy;
    private String createdRole;
    private ReceptionistDto approvedBy;

    private String approvalStatus;
    private Instant approvedAt;
    private RelativeResponseDto relative;
}
