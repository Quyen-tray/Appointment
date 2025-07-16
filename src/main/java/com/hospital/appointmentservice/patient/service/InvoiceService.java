package com.hospital.appointmentservice.patient.service;

import com.hospital.appointmentservice.patient.dto.InvoiceDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface InvoiceService {
    List<InvoiceDto> getInvoicesByPatientId(UUID patientId, LocalDate fromDate, LocalDate toDate);
    boolean payInvoice(UUID invoiceId);

}

