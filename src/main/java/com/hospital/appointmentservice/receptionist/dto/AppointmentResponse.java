package com.hospital.appointmentservice.receptionist.dto;

import com.hospital.appointmentservice.admin.dto.RoomDto;
import com.hospital.appointmentservice.admin.model.Doctor;
import com.hospital.appointmentservice.admin.model.Receptionist;
import com.hospital.appointmentservice.admin.model.Room;
import com.hospital.appointmentservice.auth.dto.UserAccountDto;
import com.hospital.appointmentservice.auth.model.UserAccount;
import com.hospital.appointmentservice.doctor.dto.DoctorDetailDto;
import com.hospital.appointmentservice.patient.dto.PatientDto;
import com.hospital.appointmentservice.patient.entity.Patient;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;

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
}
