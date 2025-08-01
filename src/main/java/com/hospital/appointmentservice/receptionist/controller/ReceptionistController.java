package com.hospital.appointmentservice.receptionist.controller;

import com.hospital.appointmentservice.patient.dto.UpdateProfileRequestDto;
import com.hospital.appointmentservice.receptionist.dto.ReceptionistProfileDTO;
import com.hospital.appointmentservice.receptionist.service.impl.ReceptionistServiceImp;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/receptionist")
// Annotation gen constructor
@RequiredArgsConstructor
// Annotation make field private and final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReceptionistController {
    ReceptionistServiceImp receptionistService;

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

    @GetMapping("/profile")
    public ResponseEntity<?> getProfileByUserName(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập!");
        }
        String username = principal.getName();
        ReceptionistProfileDTO profile = receptionistService.getProfileByUserName(username);
        return ResponseEntity.ok(profile);
    }


    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequestDto dto, Principal principal) {
        try {
            // String username = "patient01";
            String username = principal.getName();
            receptionistService.updateProfile(username, dto);
            return ResponseEntity.ok("Cập nhật thành công!");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
