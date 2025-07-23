package com.hospital.appointmentservice.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequestDto {
    
    private String oldPassword ;
    private String newPassword ;
    private String confirmPassword;

}
