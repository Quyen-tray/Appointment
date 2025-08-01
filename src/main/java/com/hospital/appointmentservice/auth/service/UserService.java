package com.hospital.appointmentservice.auth.service;

import com.hospital.appointmentservice.auth.dto.ResetPassDTO;

public interface UserService {
    void resetPassword(String userName, ResetPassDTO requestDto);
}
