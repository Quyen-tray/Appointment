package com.hospital.appointmentservice.patient.service;

import com.hospital.appointmentservice.patient.dto.ContactReplyHistoryDTO;
import com.hospital.appointmentservice.patient.dto.ContactUsDTO;
import com.hospital.appointmentservice.receptionist.dto.ReplyContactDTO;

import java.util.List;
import java.util.UUID;

public interface ContactUsService {
    void submitContact(ContactUsDTO dto);
    List<ContactUsDTO> getAllContacts();
    void replyToContact(ReplyContactDTO dto);
    List<ContactReplyHistoryDTO> getReplyHistoryByContactId(UUID contactId);
}
