package com.hospital.appointmentservice.patient.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalVisitDto {
    private String PatientId;
    private String id;
    private String appointmentId;
    private String doctorId;
    private String diagnosis;
    private String note;
    private String createdAt;


    // Nếu muốn thêm thông tin tên bác sĩ hoặc thông tin appointment,
    // bạn có thể thêm các field như doctorName, appointmentDate,...
    // và map thêm sau khi Include liên quan.
}
