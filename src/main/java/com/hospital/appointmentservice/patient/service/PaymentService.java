package com.hospital.appointmentservice.patient.service;

import com.hospital.appointmentservice.patient.dto.PaymentResponse;
import com.hospital.appointmentservice.patient.entity.Invoice;
import com.hospital.appointmentservice.patient.entity.Payment;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    Payment addNewPayment(UUID invoiceId);
    PaymentResponse createNewVnPayPayment(BigDecimal price, UUID billId) throws UnsupportedEncodingException;
    void updatePayment(UUID paymentId);
}
