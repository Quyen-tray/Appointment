package com.hospital.appointmentservice.patient.service.iplm;

import com.hospital.appointmentservice.auth.service.EmailService;
import com.hospital.appointmentservice.patient.dto.ContactReplyHistoryDTO;
import com.hospital.appointmentservice.patient.dto.ContactUsDTO;
import com.hospital.appointmentservice.patient.entity.ContactReplyHistory;
import com.hospital.appointmentservice.patient.entity.ContactUs;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.repository.ContactReplyHistoryRepository;
import com.hospital.appointmentservice.patient.repository.ContactUsRepository;
import com.hospital.appointmentservice.patient.repository.PatientRepository;
import com.hospital.appointmentservice.patient.service.ContactUsService;
import com.hospital.appointmentservice.receptionist.dto.ReplyContactDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ContactUsServiceImpl implements ContactUsService {

    @Autowired private ContactUsRepository contactUsRepo;
    @Autowired private PatientRepository patientRepo;
    @Autowired private EmailService emailService;
    @Autowired private ContactReplyHistoryRepository contactReplyHistoryRepo;

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
    @Override
    public List<ContactUsDTO> getAllContacts() {
        return contactUsRepo.findAll()
                .stream()
                .map(e -> ContactUsDTO.builder()
                        .id(e.getId())
                        .name(e.getName())
                        .email(e.getEmail())
                        .subject(e.getSubject())
                        .message(e.getMessage())
                        .createdAt(e.getCreatedAt())
                        .replied(e.isReplied())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void replyToContact(ReplyContactDTO dto) {
        ContactUs contact = contactUsRepo.findById(dto.getContactId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy liên hệ"));

        emailService.send(
                contact.getEmail(),
                dto.getSubject(),
                dto.getReplyMessage()
        );
        contact.setReplied(true);
        contactUsRepo.save(contact);
        ContactReplyHistory history = ContactReplyHistory.builder()
                .contact(contact)
                .subject(dto.getSubject())
                .message(dto.getReplyMessage())
                .sentAt(java.time.LocalDateTime.now())
                .build();

        contactReplyHistoryRepo.save(history);
    }

    @Override
    public List<ContactReplyHistoryDTO> getReplyHistoryByContactId(UUID contactId) {
        List<ContactReplyHistory> histories = contactReplyHistoryRepo.findByContactId(contactId);

        return histories.stream()
                .map(h -> ContactReplyHistoryDTO.builder()
                        .id(h.getId())
                        .subject(h.getSubject())
                        .message(h.getMessage())
                        .sentAt(h.getSentAt())
                        .build())
                .collect(Collectors.toList());
    }
}

