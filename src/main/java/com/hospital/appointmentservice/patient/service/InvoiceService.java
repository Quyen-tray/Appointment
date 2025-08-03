package com.hospital.appointmentservice.patient.service;

import com.hospital.appointmentservice.patient.dto.InvoiceDto;
import com.hospital.appointmentservice.patient.dto.NotifyUnpaidDto;
import com.hospital.appointmentservice.patient.dto.PaymentResponse;
import com.hospital.appointmentservice.patient.dto.UnpaidInvoiceDto;
import com.hospital.appointmentservice.patient.entity.Invoice;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface InvoiceService {
    List<InvoiceDto> getInvoicesByPatientId(UUID patientId, LocalDate fromDate, LocalDate toDate);
    boolean payInvoice(UUID invoiceId);
    Invoice addNewInvoice(UUID appointmentId);
    Invoice getInvoiceById(UUID invoiceId);
    List<InvoiceDto> getUnpaidInvoice(UUID patientId);
    void sendEmail(NotifyUnpaidDto invoices);
    List<UnpaidInvoiceDto> getAllUnpaidInvoice();
}

