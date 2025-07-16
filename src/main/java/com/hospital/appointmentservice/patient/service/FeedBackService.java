package com.hospital.appointmentservice.patient.service;

import com.hospital.appointmentservice.patient.dto.FeedBackDto;

import java.util.List;
import java.util.UUID;

public interface FeedBackService {
    List<FeedBackDto> getAllFeedBack(int page, int size );
    FeedBackDto getFeedBackById(UUID id);
    List<FeedBackDto> getFeedBackByPatientId(UUID patientId);
    List<FeedBackDto> getFeedBackByDoctorId(UUID doctorId);
    FeedBackDto createFeedBack(FeedBackDto dto);
    FeedBackDto updateFeedBack(UUID id, FeedBackDto dto);
    boolean deleteFeedBack(UUID id);
}
