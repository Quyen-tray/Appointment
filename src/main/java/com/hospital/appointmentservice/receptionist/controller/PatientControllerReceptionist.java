package com.hospital.appointmentservice.receptionist.controller;

import com.hospital.appointmentservice.patient.entity.Patient;
import com.hospital.appointmentservice.receptionist.dto.PatientDetailDTO;
import com.hospital.appointmentservice.receptionist.dto.PatientHistoryDTO;
import com.hospital.appointmentservice.receptionist.dto.PatientRegisterRequest;
import com.hospital.appointmentservice.receptionist.dto.PatientUpdateDTO;
import com.hospital.appointmentservice.receptionist.service.ReceptionistPatientService;
import com.hospital.appointmentservice.receptionist.service.impl.ReceptionistPatientServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api")
public class PatientControllerReceptionist {
    @Autowired
    private ReceptionistPatientService receptionistService;

    @Autowired
    private ReceptionistPatientServiceImp patientService;

    @GetMapping("/all")
    public ResponseEntity<List<PatientDetailDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }
    @GetMapping("/{patientId}/history")
    public List<PatientHistoryDTO> getPatientHistory(@PathVariable UUID patientId) {
        return patientService.getPatientHistory(patientId);
    }
    @PutMapping("/patients/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable UUID id, @RequestBody PatientUpdateDTO dto) {
        try {
            Patient patient = receptionistService.getPatientById(id);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy bệnh nhân");
            }

            // Cập nhật thông tin
            patient.setFullName(dto.getFullName());
            patient.setDob(dto.getDob());
            patient.setGender(dto.getGender());
            patient.setPhone(dto.getPhone());
            patient.setEmail(dto.getEmail());
            patient.setAddress(dto.getAddress());

            receptionistService.savePatient(patient);

            // Trả về DTO (hoặc message)
            return ResponseEntity.ok("Cập nhật thành công");
        } catch (Exception e) {
            e.printStackTrace(); // để log ra lỗi chi tiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi cập nhật bệnh nhân");
        }
    }

    @PostMapping("/patients")
    public ResponseEntity<String> createPatient(@RequestBody PatientRegisterRequest request) {
        try {
            receptionistService.addNewPatient(request);
            return ResponseEntity.ok("Thêm bệnh nhân thành công!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi trong quá trình xử lý.");
        }
    }


}
