package com.hospital.appointmentservice.patient.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedBackDto {
    private String id;         // UUID dưới dạng chuỗi
    private String patientId;  // UUID dưới dạng chuỗi
    private String doctorId;   // UUID dưới dạng chuỗi
    private Integer score;
    private String comment;
}
