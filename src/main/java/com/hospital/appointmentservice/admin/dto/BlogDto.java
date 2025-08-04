package com.hospital.appointmentservice.admin.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Getter
@Setter
public class BlogDto {
    private UUID id;
    private String postData;
    private String image;
    private LocalDateTime dateCreate;
    private LocalDateTime updateDate;
    private String title;
}
