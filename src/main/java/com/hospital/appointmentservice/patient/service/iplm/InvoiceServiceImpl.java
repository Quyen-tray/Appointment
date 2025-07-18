package com.hospital.appointmentservice.patient.service.iplm;

import com.hospital.appointmentservice.patient.dto.InvoiceDto;
import com.hospital.appointmentservice.patient.repository.InvoiceRepository;
import com.hospital.appointmentservice.patient.service.InvoiceService;
import com.hospital.appointmentservice.patient.entity.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public List<InvoiceDto> getInvoicesByPatientId(UUID patientId, LocalDate fromDate, LocalDate toDate) {
        return invoiceRepository.findByPatientId(patientId).stream()
                .filter(invoice -> {
                    LocalDate issued = invoice.getIssuedDate().toLocalDate();
                    return (fromDate == null || !issued.isBefore(fromDate)) &&
                            (toDate == null || !issued.isAfter(toDate));
                })
                .sorted(Comparator.comparing(Invoice::getIssuedDate).reversed())
                .map(invoice -> {
                    InvoiceDto dto = new InvoiceDto();
                    dto.setId(invoice.getId());
                    dto.setAmount(invoice.getAmount());
                    dto.setIssuedDate(invoice.getIssuedDate());
                    dto.setStatus(invoice.getStatus()); // Để nguyên status trong DB
                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Override
    public boolean payInvoice(UUID invoiceId) {
        return invoiceRepository.findById(invoiceId).map(invoice -> {
            if (invoice.getStatus() != null && invoice.getStatus().equalsIgnoreCase("Đã thanh toán")) {
                return false; // Đã thanh toán rồi
            }
            invoice.setStatus("Đã thanh toán");
            invoiceRepository.save(invoice);
            return true;
        }).orElse(false);
    }
}
