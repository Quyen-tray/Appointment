package com.hospital.appointmentservice.auth.controller;

import com.hospital.appointmentservice.auth.dto.ResetPassDTO;
import com.hospital.appointmentservice.auth.dto.UserAccountDto;
import com.hospital.appointmentservice.auth.security.JwtUtil;
import com.hospital.appointmentservice.auth.service.UserService;
import com.hospital.appointmentservice.patient.dto.ChangePasswordRequestDto;
import com.hospital.appointmentservice.patient.repository.PatientRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final JwtUtil jwtUtil;
    private final PatientRepository patientRepository;
    private final UserService userService;


    public UserController(JwtUtil jwtUtil, PatientRepository patientRepository, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.patientRepository = patientRepository;
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserAccountDto> getUser(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(null);
        }
        String token = authorization.substring("Bearer ".length());
        Claims user = jwtUtil.getAllClaims(token);
        String userIdStr = user.get("id", String.class);
        UUID userId = UUID.fromString(userIdStr);

        UserAccountDto userDto = new UserAccountDto();
        userDto.setUsername(user.getSubject());
        userDto.setId(userId);
        userDto.setRoles(user.get("role", String.class));
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/change-pass")
    public ResponseEntity<?> changePass(HttpServletRequest request, @RequestBody ResetPassDTO requestDto) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
        }

        String token = authorization.substring("Bearer ".length());
        Claims userClaims = jwtUtil.getAllClaims(token);
        String username = userClaims.getSubject();
        userService.resetPassword(username, requestDto);
        return ResponseEntity.ok("Change password successfully");

    }
}