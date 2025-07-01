package com.hospital.appointmentservice.auth.controller;

import com.hospital.appointmentservice.auth.dto.UserAccountDto;
import com.hospital.appointmentservice.auth.security.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/me")
    public ResponseEntity<UserAccountDto> getUser(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(null);
        }
        String token = authorization.substring("Bearer ".length());
        Claims user = jwtUtil.getAllClaims(token);
        UserAccountDto userDto = new UserAccountDto();
        userDto.setUsername(user.getSubject());
        userDto.setRoles(user.get("role", String.class));
        return ResponseEntity.ok(userDto);
    }
}
