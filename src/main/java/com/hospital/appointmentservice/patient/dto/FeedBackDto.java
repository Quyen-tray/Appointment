package com.hospital.appointmentservice.patient.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedBackDto {
    private String id;         // UUID dưới dạng chuỗi
    private String patientId;  // UUID dưới dạng chuỗi
    private String patientName;
    private String doctorId;   // UUID dưới dạng chuỗi
    private String doctorName;
    private Integer score;
    private String comment;
    private Date created;
}
