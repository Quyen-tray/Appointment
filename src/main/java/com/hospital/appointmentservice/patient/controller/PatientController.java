package com.hospital.appointmentservice.patient.controller;

import com.hospital.appointmentservice.admin.model.Appointment;
import com.hospital.appointmentservice.patient.dto.*;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.repository.AppointmentRepository;
import com.hospital.appointmentservice.patient.repository.PatientRepository;
import com.hospital.appointmentservice.patient.service.PatientAppointmentService;
import com.hospital.appointmentservice.patient.service.PatientService;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hospital.appointmentservice.patient.service.InvoiceService;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    private final PatientService patientService;
    private final InvoiceService invoiceService;
    private final PatientRepository patientRepository;
    private final PatientAppointmentService appointmentService;

    public PatientController(AppointmentRepository appointmentRepository, PatientService patientService,
            InvoiceService invoiceService, PatientAppointmentService appointmentService,
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
    public ResponseEntity<?> getMyAppointments(Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String doctorName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String examiner) {
        String username = principal.getName();
        Patient patient = patientRepository.findByUser_Username(username);

        if (patient == null) {
            return ResponseEntity.notFound().build();
        }

        doctorName = doctorName != null ? doctorName.trim() : null;
        Pageable pageable = PageRequest.of(page, size, Sort.by("scheduledTime").descending());
        Page<Appointment> appointmentPage = appointmentService.getAppointmentsByPatientId(patient.getId(), doctorName,
                startDate, endDate, status, examiner, pageable);

        Page<AppointmentDto> dtoPage = appointmentPage.map(appointment -> {
            AppointmentDto dto = new AppointmentDto();
            dto.setId(appointment.getId());
            dto.setPatientName(username);
            dto.setStatus(appointment.getStatus());
            dto.setScheduledTime(appointment.getScheduledTime().toString());
            dto.setDoctorName(appointment.getDoctor().getFullName());
            dto.setRoomName(
                    appointment.getRoom() != null ? appointment.getRoom().getRoomName() : "Chưa có phòng!");
            if (appointment.getRelative() != null) {
                RelativeDto relativeDto = new RelativeDto();
                relativeDto.setId(appointment.getRelative().getId());
                relativeDto.setFullName(appointment.getRelative().getFullName());
                relativeDto.setDob(appointment.getRelative().getDob());
                relativeDto.setGender(appointment.getRelative().getGender());
                relativeDto.setRelation(appointment.getRelative().getRelation());
                relativeDto.setNote(appointment.getRelative().getNote());
                dto.setRelative(relativeDto);
            } else {
                dto.setPatientName(appointment.getPatient().getFullName());
            }
            return dto;
        });

        Map<String, Object> response = new HashMap<>();
        response.put("appointments", dtoPage.getContent());
        response.put("currentPage", dtoPage.getNumber());
        response.put("totalItems", dtoPage.getTotalElements());
        response.put("totalPages", dtoPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/reject-appointment/{appointmentId}")
    public ResponseEntity<?> rejectAppointment(@PathVariable UUID appointmentId, Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập!");
        }
        String username = principal.getName();
        Patient patient = patientRepository.findByUser_Username(username);

        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy bệnh nhân!");
        }

        Appointment appointment = appointmentService.getAppointmentById(appointmentId);

        if (appointment == null || !appointment.getPatient().getId().equals(patient.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền hủy lịch hẹn này");
        }

        if (!"PENDING".equalsIgnoreCase(appointment.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Chỉ được hủy lịch hẹn ở trạng thái Pending!");
        }

        appointment.setStatus("CANCELLED");
        appointmentService.saveAppointment(appointment);

        return ResponseEntity.ok("Đã hủy lịch hẹn thành công!");

    }

    @PostMapping("/book-appointments")
    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentRequestDto dto, Principal principal) {

        try {
            String username = principal.getName();
            Appointment appointment = appointmentService.createAppointment(username, dto);
            AppointmentBookingResponeDto respone = new AppointmentBookingResponeDto("Đã đặt lịch thành công!",
                    appointment.getId());

            return ResponseEntity.ok(respone);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/appointment-detail/{id}")
    public ResponseEntity<?> getAppointmentDetailById(@PathVariable UUID id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập!");
        }

        String username = principal.getName();
        try {
            AppointmentDto dto = appointmentService.getAppointmentDetailById(id, username);
            return ResponseEntity.ok(dto);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/updateTime-appointment/{id}")
    public ResponseEntity<?> updateAppointmentTime(@PathVariable UUID id, @RequestBody AppointmentUpdateTimeDto dto,
            Principal principal) {

        String username = principal.getName();
        Patient patient = patientRepository.findByUser_Username(username);

        if (patient == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không tìm thấy bệnh nhân!");
        }

        try {
            appointmentService.updateScheduledTime(id, patient.getId(), dto.getScheduledTime());
            return ResponseEntity.ok("Cập nhật thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    // view profile
    @GetMapping("/profile")
    public ResponseEntity<?> getProfileByUserName(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập!");
        }
        String username = principal.getName();
        PatientProfileDto profile = patientService.getProfileByUserName(username);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequestDto dto, Principal principal) {
        try {
            System.out.println("==> DTO in Controller: " + dto);
            String username = principal.getName();
            patientService.updateProfile(username, dto);
            return ResponseEntity.ok("Cập nhật thành công!");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestDto dto, Principal principal) {
        String username = principal.getName();
        patientService.changePasswordRequest(username, dto);
        return ResponseEntity.ok("Đổi mật khẩu thành công!");
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
    }

    @GetMapping("/visits/{id}")
    public ResponseEntity<List<MedicalVisitDto>> getVisitsOfPatient(@PathVariable("id") UUID id) {
        List<MedicalVisitDto> visits = patientService.getMedicalVisitsByPatientId(id);
        if (visits == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(visits);
    }

    @GetMapping("/invoices/{id}")
    public ResponseEntity<List<InvoiceDto>> getInvoicesOfPatient(@PathVariable("id") UUID id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        List<InvoiceDto> invoices = invoiceService.getInvoicesByPatientId(id, fromDate, toDate);
        if (invoices == null || invoices.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(invoices);
    } // http://localhost:8081/api/patient/invoices/{patientId}

    @PutMapping("/pay/{invoiceId}")
    public ResponseEntity<?> payInvoice(@PathVariable UUID invoiceId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập!");
        }

        boolean success = invoiceService.payInvoice(invoiceId);
        if (!success) {
            return ResponseEntity.badRequest().body("Không thể thanh toán hóa đơn này.");
        }

        return ResponseEntity.ok("Thanh toán thành công!");
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<?> getPatientByUserId(@PathVariable("userId") UUID userId) {
        List<Patient> patients = patientRepository.findAllByUser_Id(userId);

        if (patients.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy bệnh nhân với userId: " + userId);
        }
        Optional<Patient> optionalValidPatient = patients.stream()
                .filter(p -> p.getFullName() != null)
                .findFirst();

        if (optionalValidPatient.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Tìm thấy nhiều bệnh nhân nhưng không có bản ghi hợp lệ.");
        }

        Patient patient = optionalValidPatient.get();
        PatientDto dto = new PatientDto();
        dto.setId(patient.getId() != null ? patient.getId().toString() : null);
        dto.setUserId(patient.getUser() != null ? patient.getUser().getId().toString() : null);
        dto.setFullName(patient.getFullName());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/invoices/unpaid/{id}")
    public ResponseEntity<List<InvoiceDto>> getInvoiceUnpaid(@PathVariable("id") UUID id) {
        List<InvoiceDto> invoices = invoiceService.getUnpaidInvoice(id);
        if (invoices == null || invoices.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(invoices);
    }
}
