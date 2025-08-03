package com.hospital.appointmentservice.admin.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Getter
@Setter
public class AuditBlogDto {
    private String postData;
    private String image;
    private String title;
}
