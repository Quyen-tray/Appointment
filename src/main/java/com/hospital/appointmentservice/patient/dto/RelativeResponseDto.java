package com.hospital.appointmentservice.patient.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class RelativeResponseDto {
    private UUID id;
    private String fullName;
    private String relation;

    public RelativeResponseDto(UUID id, String fullName, String relation) {
        this.id = id;
        this.fullName = fullName;
        this.relation = relation;
    }
}
