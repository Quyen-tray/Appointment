package com.hospital.appointmentservice.receptionist.controller;


import com.hospital.appointmentservice.receptionist.service.ReceptionistPatientService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/receptionist")
// Annotation gen constructor
@RequiredArgsConstructor
// Annotation make field private and final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReceptionistController {
    ReceptionistPatientService receptionistService;

    @GetMapping("")
    public ResponseEntity<?> getAllReceptionists() {
//        try {
//            List<ReceptionistDto> receptionists = receptionistService.getAllReceptionist();
//
//            return ResponseEntity.status(HttpStatus.OK).body(receptionists);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errors in get receptinists process" + e.getMessage());
//        }
return null;
    }
}
