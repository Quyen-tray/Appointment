package com.hospital.appointmentservice.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hospital.appointmentservice.auth.dto.UserAccountDto;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Getter
@Setter
@NoArgsConstructor
public class PatientAdminDto {
    private UUID id;
    private UserAccountDto userAccountDto;
    private String fullName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @Size(max = 50)
    private String gender;

    private String phone;
    private String email;
    private String insuranceId;

    private String avatar ;

    public PatientAdminDto(UUID id, UserAccountDto userAccountDto, String fullName, LocalDate dob, String gender, String phone, String email, String insuranceId, String avatar) {
        this.id = id;
        this.userAccountDto = userAccountDto;
        this.fullName = fullName;
        this.dob = dob;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.insuranceId = insuranceId;
        this.avatar = avatar;
    }

    public PatientAdminDto(UUID id, UserAccountDto userAccountDto, String fullName, LocalDate dob, String gender, String phone, String email, String insuranceId) {
        this.id = id;
        this.userAccountDto = userAccountDto;
        this.fullName = fullName;
        this.dob = dob;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.insuranceId = insuranceId;
    }
}
