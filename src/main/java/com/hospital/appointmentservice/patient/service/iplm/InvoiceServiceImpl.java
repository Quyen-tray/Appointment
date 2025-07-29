package com.hospital.appointmentservice.patient.service.iplm;

import com.hospital.appointmentservice.admin.model.Appointment;
import com.hospital.appointmentservice.patient.dto.InvoiceDto;
import com.hospital.appointmentservice.patient.entity.LabRequest;
import com.hospital.appointmentservice.patient.entity.MedicalVisit;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.repository.*;
import com.hospital.appointmentservice.patient.service.InvoiceService;
import com.hospital.appointmentservice.patient.entity.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository,
                              LabRequestRepository labRequestRepository,
                              AppointmentRepository appointmentRepository,
                              MedicalVisitRepository medicalVisitRepository,
                            PatientRepository patientRepository) {
        this.invoiceRepository = invoiceRepository;
        this.appointmentRepository = appointmentRepository;
        this.labRequestRepository = labRequestRepository;
        this.medicalVisitRepository = medicalVisitRepository;
        this.patientRepository = patientRepository;
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
        if(appointment == null){
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
}
