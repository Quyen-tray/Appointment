package com.hospital.appointmentservice.auth.service;

import com.hospital.appointmentservice.auth.dto.ResetPassDTO;
import com.hospital.appointmentservice.patient.dto.ChangePasswordRequestDto;

public interface UserService {
    void resetPassword(String userName, ResetPassDTO requestDto);
}
