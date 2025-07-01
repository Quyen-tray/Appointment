package com.hospital.appointmentservice.patient.controller;

import com.hospital.appointmentservice.patient.dto.PatientDto;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hospital.appointmentservice.patient.dto.MedicalVisitDto;
import com.hospital.appointmentservice.patient.dto.InvoiceDto;
import com.hospital.appointmentservice.patient.service.InvoiceService;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/patient")
public class PatientController {
    private final PatientService patientService;
    private final InvoiceService invoiceService;
    private final PatientRepository patientRepository;
    private final PatientAppointmentService appointmentService;


    @Autowired
    public PatientController(PatientService patientService, InvoiceService invoiceService, PatientAppointmentService appointmentService,
                             PatientRepository patientRepository) {
        this.patientService = patientService;
        this.invoiceService = invoiceService;
        this.appointmentService = appointmentService;
        this.patientRepository = patientRepository;
    }

    @PostMapping("/add")
    public Patient addPatient(@RequestBody PatientDto patientDto) {
        return patientService.addPatient(patientDto);
    }

    @GetMapping("/my-appointment")
    public ResponseEntity<?> getMyAppointments() {
        String username = "patient01";
        Patient patient = patientRepository.findByUser_Username(username);

        if (patient == null) {
            return ResponseEntity.notFound().build();
        }

        List<Appointment> list = appointmentService.getAppointmentsByPatientId(patient.getId());

        List<AppointmentDto> dtos = list.stream().map(appointment -> {
            AppointmentDto dto = new AppointmentDto();
            dto.setId(appointment.getId());
            dto.setPatientName(username);
            dto.setStatus(appointment.getStatus());
            dto.setScheduledTime(appointment.getScheduledTime().toString());
            dto.setDoctorName(appointment.getDoctor().getFullName());
            dto.setRoomName(
                    appointment.getRoom() != null ? appointment.getRoom().getName() : "Chưa có phòng!");
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
    @PostMapping("/reject-appointment")
    public ResponseEntity<?> rejectAppointment(@RequestParam UUID appointmentId, Principal principal) {
        // đăng nhập thì bỏ cmt
        // if(principal == null){
        // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng
        // nhập!");
        // }
        // String username = principal.getName();
        String username = "patient01";
        Patient patient = patientRepository.findByUser_Username(username);

        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy bệnh nhân!");
        }

        Appointment appointment = appointmentService.getAppointmentById(appointmentId);

        if (appointment == null || !appointment.getPatient().getId().equals(patient.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền hủy lịch hẹn này");
        }

        if (!"Pending".equalsIgnoreCase(appointment.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Chỉ được hủy lịch hẹn ở trạng thái Pending!");
        }

        appointment.setStatus("Cancelled");
        appointmentService.saveAppointment(appointment);

        return ResponseEntity.ok("Đã hủy lịch hẹn thành công!");

    }

    @PostMapping("/appointments")
    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentRequestDto dto, Principal principal) {

        try {
            // String username = principal.getName(); // lấy từ token / session
            String username = "patient01";
            appointmentService.createAppointment(username, dto);
            return ResponseEntity.ok("Đã đăng ký thành công!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }

    }

    @PutMapping("/my-appointment/{id}")
    public ResponseEntity<?> updateAppointmentTime( @PathVariable UUID id ,@RequestBody AppointmentUpdateTimeDto dto , Principal principal ){

        //có đăng nhập dùng principal
        //  String username = principal.getName();
        String username = "patient01";
        Patient patient = patientRepository.findByUser_Username(username);

        if(patient == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không tìm thấy bệnh nhân!");
        }

        try {
            appointmentService.updateScheduledTime(id, patient.getId(), dto.getScheduledTime());
            return ResponseEntity.ok("Cập nhật thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        List<PatientDto> patients = patientService.getPatients();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable("id") UUID id) {
        PatientDto dto = patientService.getPatientById(id);
        return dto != null
                ? ResponseEntity.ok(dto)
                : ResponseEntity.notFound().build();
    } //http://localhost:8081/api/patient/Id

    @GetMapping("/visits/{id}")
    public ResponseEntity<List<MedicalVisitDto>> getVisitsOfPatient(@PathVariable("id") UUID id) {
        List<MedicalVisitDto> visits = patientService.getMedicalVisitsByPatientId(id);
        if (visits == null) {
            return ResponseEntity.notFound().build(); // patient không tồn tại
        }
        return ResponseEntity.ok(visits);
    }

    @GetMapping("/invoices/{id}")
    public ResponseEntity<List<InvoiceDto>> getInvoicesOfPatient(@PathVariable("id") UUID id) {
        List<InvoiceDto> invoices = invoiceService.getInvoicesByPatientId(id);
        if (invoices == null || invoices.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(invoices);
    } //http://localhost:8081/api/patient/invoices/{patientId}
}
