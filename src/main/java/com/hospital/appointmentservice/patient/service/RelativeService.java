package com.hospital.appointmentservice.patient.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.hospital.appointmentservice.patient.dto.RelativeDto;
import com.hospital.appointmentservice.patient.dto.RelativeResponseDto;

public interface RelativeService {
    List<RelativeDto> getRelativesByUsername(String username);

    RelativeDto addRelative(String username, RelativeDto dto);

    RelativeDto updateRelative(UUID id, RelativeDto dto, String username);

    void deleteRelative(UUID id, String username);

    List<RelativeResponseDto> getRelativesSummaryByUsername(String username);

    Page<RelativeDto> getPagedRelativesWithFullInfo(String username, int page, int size, String search);

}
