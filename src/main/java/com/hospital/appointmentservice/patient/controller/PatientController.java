package com.hospital.appointmentservice.patient.controller;

import com.hospital.appointmentservice.patient.dto.AppointmentDto;
import com.hospital.appointmentservice.patient.dto.AppointmentRequestDto;
import com.hospital.appointmentservice.patient.dto.PatientDto;
import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.patient.repository.PatientRepository;
import com.hospital.appointmentservice.patient.service.PatientAppointmentService;
import com.hospital.appointmentservice.patient.service.PatientService;
import com.hospital.appointmentservice.admin.model.Appointment;

import java.security.Principal;
import java.util.UUID;
import java.util.stream.Collectors;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patient")
public class PatientController {
    private final PatientService patientService;
     private final PatientAppointmentService appointmentService;
    private final PatientRepository patientRepository;

    @Autowired
    public PatientController(PatientService patientService, PatientAppointmentService appointmentService,
            PatientRepository patientRepository) {
        this.patientService = patientService;
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
}
