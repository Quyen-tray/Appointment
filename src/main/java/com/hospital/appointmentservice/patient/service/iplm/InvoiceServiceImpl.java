package com.hospital.appointmentservice.patient.service.iplm;

import com.hospital.appointmentservice.admin.model.Appointment;
import com.hospital.appointmentservice.auth.service.EmailService;
import com.hospital.appointmentservice.patient.dto.InvoiceDto;
import com.hospital.appointmentservice.patient.dto.NotifyUnpaidDto;
import com.hospital.appointmentservice.patient.dto.UnpaidInvoiceDto;
import com.hospital.appointmentservice.patient.entity.LabRequest;
import com.hospital.appointmentservice.patient.entity.MedicalVisit;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.repository.*;
import com.hospital.appointmentservice.patient.service.InvoiceService;
import com.hospital.appointmentservice.patient.entity.Invoice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final LabRequestRepository labRequestRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicalVisitRepository medicalVisitRepository;
    private final PatientRepository patientRepository;
    private final EmailService emailService;
    
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository,
                              LabRequestRepository labRequestRepository,
                              AppointmentRepository appointmentRepository,
                              MedicalVisitRepository medicalVisitRepository,
                              PatientRepository patientRepository,
                              EmailService emailService) {
        this.invoiceRepository = invoiceRepository;
        this.appointmentRepository = appointmentRepository;
        this.labRequestRepository = labRequestRepository;
        this.medicalVisitRepository = medicalVisitRepository;
        this.patientRepository = patientRepository;
        this.emailService = emailService;
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

    @Override
    public Invoice addNewInvoice(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).get();
        appointment.setStatus("DONE");
        appointmentRepository.save(appointment);
        if (appointment == null) {
            return null;
        }
        MedicalVisit medicalVisit = medicalVisitRepository.findByAppointment_Id(appointmentId);

        List<LabRequest> labRequests = labRequestRepository.findByVisit_Id(medicalVisit.getId());
        Invoice invoice = new Invoice();
        invoice.setStatus("UNPAID");
        BigDecimal totalAmount = labRequests.stream()
                .map(lr -> lr.getPrice() != null ? lr.getPrice() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        invoice.setAmount(totalAmount);
        invoice.setPatient(appointment.getPatient());
        invoice.setIssuedDate(LocalDateTime.now());
        invoiceRepository.save(invoice);
        return invoice;

    }

    @Override
    public Invoice getInvoiceById(UUID invoiceId) {
        return invoiceRepository.findById(invoiceId).get();
    }

    @Override
    public List<InvoiceDto> getUnpaidInvoice(UUID userId) {
        Patient patient = patientRepository.findByUser_Id(userId).get();
        return invoiceRepository.findByPatientId(patient.getId()).stream()
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
    @Transactional
    public void sendEmail(NotifyUnpaidDto notifies) {
        if (notifies == null || notifies.getInvoiceIds() == null || notifies.getInvoiceIds().isEmpty()) {
            return; // No invoices to process
        }

        for (UUID invoiceId : notifies.getInvoiceIds()) {
            try {
                // Find the invoice by ID
                Invoice invoice = invoiceRepository.findById(invoiceId)
                        .orElseThrow(() -> new RuntimeException("Invoice not found with ID: " + invoiceId));

                // Check if the invoice is unpaid
                if ("Đã thanh toán".equalsIgnoreCase(invoice.getStatus())) {
                    continue; // Skip already paid invoices
                }

                // Get patient and their email
                Patient patient = invoice.getPatient();
                if (patient == null || patient.getUser() == null || patient.getEmail() == null) {
                    continue; // Skip if patient or email not found
                }

                String patientEmail = patient.getEmail();
                String patientName = patient.getFullName() != null ? patient.getFullName() : "Quý khách";

                // Prepare email content
                String subject = "Nhắc nhở thanh toán hóa đơn #" + invoiceId;

                String content = String.format(
                        "Kính gửi %s,%n%n" +
                                "Hệ thống ghi nhận hóa đơn #%s của Quý khách chưa được thanh toán.%n" +
                                "Số tiền cần thanh toán: %s VND%n" +
                                "Ngày phát hành: %s%n%n" +
                                "Vui lòng thanh toán sớm để tránh gián đoạn dịch vụ.%n%n" +
                                "Trân trọng,%nGroup 1",
                        patientName,
                        invoiceId,
                        invoice.getAmount() != null ? invoice.getAmount().toString() : "0",
                        invoice.getIssuedDate() != null ? invoice.getIssuedDate().toString() : "N/A"
                );

                // Send email
                emailService.send(patientEmail, subject, content);

                // Log the notification (optional)
                System.out.println("Sent payment reminder for invoice #" + invoiceId + " to " + patientEmail);

            } catch (Exception e) {
                // Log the error but continue with other invoices
                System.err.println("Error processing invoice #" + invoiceId + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<UnpaidInvoiceDto> getAllUnpaidInvoice() {
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoices.stream()
                .filter(invoice -> invoice.getStatus().equals("UNPAID")) // lọc unpaid
                .sorted(Comparator.comparing(Invoice::getIssuedDate).reversed())
                .map(invoice -> {
                    UnpaidInvoiceDto dto = new UnpaidInvoiceDto();
                    dto.setId(invoice.getId());
                    dto.setAmount(invoice.getAmount());
                    dto.setIssuedDate(invoice.getIssuedDate());
                    dto.setStatus(invoice.getStatus());
                    dto.setPatientName(invoice.getPatient().getFullName());
                    dto.setEmail(invoice.getPatient().getEmail());
                    dto.setPhone(invoice.getPatient().getPhone());
                    return dto;
                })
                .collect(Collectors.toList());

    }
}
