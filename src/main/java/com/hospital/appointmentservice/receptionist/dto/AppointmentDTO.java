package com.hospital.appointmentservice.receptionist.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class AppointmentDTO {
    @NotNull
    private UUID patientId;

    @NotNull
    private UUID doctorId;

    @NotNull
    private UUID roomId;

    @NotNull
    private LocalDateTime scheduledTime;

    private String status;

    @NotNull
    private UUID createdById;

    @Size(max = 50)
    private String createdRole;

    private UUID approvedById;

    @Size(max = 50)
    private String approvalStatus;

    private Instant approvedAt;
}
