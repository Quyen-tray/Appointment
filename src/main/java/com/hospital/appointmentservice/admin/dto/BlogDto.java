package com.hospital.appointmentservice.admin.dto;

import com.hospital.appointmentservice.auth.model.UserAccount;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
