package com.hospital.appointmentservice.patient.service.iplm;

import com.hospital.appointmentservice.patient.dto.ContactUsDTO;
import com.hospital.appointmentservice.patient.entity.ContactUs;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.repository.ContactUsRepository;
import com.hospital.appointmentservice.patient.repository.PatientRepository;
import com.hospital.appointmentservice.patient.service.ContactUsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactUsServiceImpl implements ContactUsService {

    @Autowired private ContactUsRepository contactUsRepo;
    @Autowired private PatientRepository patientRepo;

    @Override
    public void submitContact(ContactUsDTO dto) {
        ContactUs entity = ContactUs.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .subject(dto.getSubject())
                .message(dto.getMessage())
                .createdAt(java.time.LocalDateTime.now())
                .build();
        contactUsRepo.save(entity);

    }
}

