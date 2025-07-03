package com.hospital.appointmentservice.patient.service.iplm;



import com.hospital.appointmentservice.patient.dto.InvoiceDto;
import com.hospital.appointmentservice.patient.repository.InvoiceRepository;
import com.hospital.appointmentservice.patient.service.InvoiceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public List<InvoiceDto> getInvoicesByPatientId(UUID patientId) {
        return invoiceRepository.findByPatientId(patientId).stream().map(invoice -> {
            InvoiceDto dto = new InvoiceDto();
            dto.setId(invoice.getId());
            dto.setAmount(invoice.getAmount());
            dto.setStatus(invoice.getStatus());
            dto.setIssuedDate(invoice.getIssuedDate());
            return dto;
        }).collect(Collectors.toList());
    }
}

