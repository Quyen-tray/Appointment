package com.hospital.appointmentservice.auth.controller;

import com.hospital.appointmentservice.auth.dto.UserAccountDto;
import com.hospital.appointmentservice.auth.model.LoginRequest;
import com.hospital.appointmentservice.auth.model.LoginResponse;
import com.hospital.appointmentservice.auth.security.JwtUtil;
import com.hospital.appointmentservice.auth.service.AuthService;
import com.hospital.appointmentservice.auth.service.Login_auditService;
import com.hospital.appointmentservice.patient.dto.PatientDto;
import com.hospital.appointmentservice.patient.service.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final PatientService patientService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final Login_auditService login_auditService;

    @Autowired
    public AuthController(AuthService authService,
                          PatientService patientService,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          Login_auditService login_auditService) {
        this.authService = authService;
        this.patientService = patientService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.login_auditService = login_auditService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserAccountDto userAccountDto) {
        try {
            UserAccountDto userAccount = new UserAccountDto(userAccountDto.getUsername(),
                    userAccountDto.getPassword(),
                    userAccountDto.getEmail(),
                    "PATIENT"
            );
            //valid name existed in database table user account
            if(authService.existsByUserName(userAccountDto.getUsername())){
                return  ResponseEntity.status(HttpStatus.CONFLICT).body("Username is already taken");
            }
            //valid email existed in database in table patient
            if(patientService.existsPatientByEmail(userAccountDto.getEmail())){
                return  ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already taken");
            }
            //save user account in database
            authService.registerUser(userAccount);

            //Create 1 record patient in patient table
            PatientDto patientDto = new PatientDto();
            patientDto.setFullName(userAccountDto.getUsername());
            patientDto.setEmail(userAccountDto.getEmail());
            patientService.addPatient(patientDto);

            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errors in register process"+e.getMessage());
        }

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest,
                                                          HttpServletRequest request) {
            // authentication  user
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
                );
                login_auditService.log(loginRequest.getUsername(),"LOGIN_SUCCESS",request);
                // authentication successfully ,create token(jwt)
                String jwt = jwtUtil.generateToken((UserDetails) authentication.getPrincipal());
                LoginResponse loginResponse = new LoginResponse(jwt);

                // return jwt for user
                return ResponseEntity.ok(loginResponse);
            } catch (BadCredentialsException e) {
                login_auditService.log(loginRequest.getUsername(),"LOGIN_FAILED",request);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }

    @PostMapping(   "/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if(authorization == null|| !authorization.startsWith("Bearer ")){
            return ResponseEntity.badRequest().body("Missing or invalid token");
        }
        String token = authorization.substring("Bearer ".length());
        String username = jwtUtil.getUsernameFromToken(token);

        login_auditService.log(username,"LOGOUT",request);
        return ResponseEntity.ok("Logged out");


    }




    }
