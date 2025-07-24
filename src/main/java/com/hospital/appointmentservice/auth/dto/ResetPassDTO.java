package com.hospital.appointmentservice.auth.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ResetPassDTO {
    private String oldPassword ;
    private String newPassword ;
}
