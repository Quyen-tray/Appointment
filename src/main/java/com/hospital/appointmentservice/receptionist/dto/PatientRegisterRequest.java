package com.hospital.appointmentservice.receptionist.dto;

import lombok.Data;

@Data
public class PatientRegisterRequest {
    private String fullName;
    private String email;
    private String phone;
    private String dob;       // dùng String vì dữ liệu từ JSON gửi lên thường ở dạng chuỗi
    private String gender;
    private String address;

    private String username;
    private String password;
}
