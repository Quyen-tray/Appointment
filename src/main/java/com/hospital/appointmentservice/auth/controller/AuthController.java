package com.hospital.appointmentservice.auth.controller;

import com.hospital.appointmentservice.auth.dto.UserAccountDto;
import com.hospital.appointmentservice.auth.model.LoginRequest;
import com.hospital.appointmentservice.auth.model.LoginResponse;
import com.hospital.appointmentservice.auth.model.UserAccount;
import com.hospital.appointmentservice.auth.repository.UserAccountRepository;
import com.hospital.appointmentservice.auth.security.CustomUserDetailsService;
import com.hospital.appointmentservice.auth.security.JwtUtil;
import com.hospital.appointmentservice.auth.security.PasswordUtil;
import com.hospital.appointmentservice.auth.service.EmailService;
import com.hospital.appointmentservice.auth.service.Login_auditService;
import com.hospital.appointmentservice.auth.service.UserAccountService;
import com.hospital.appointmentservice.patient.dto.PatientDto;
import com.hospital.appointmentservice.patient.service.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserAccountService authService;
    private final PatientService patientService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final Login_auditService login_auditService;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;
    private final EmailService emailService;
    private final UserAccountRepository userAccountRepository;

    public AuthController(UserAccountService authService,
                          PatientService patientService,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          Login_auditService login_auditService,
                          PasswordEncoder passwordEncoder,
                          CustomUserDetailsService userDetailsService,
                          EmailService emailService,
                          UserAccountRepository userAccountRepository) {
        this.authService = authService;
        this.patientService = patientService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.login_auditService = login_auditService;
        this.passwordEncoder=passwordEncoder;
        this.userDetailsService=userDetailsService;
        this.emailService=emailService;
        this.userAccountRepository=userAccountRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserAccountDto userAccountDto) {
        try {
            UserAccountDto userAccount = new UserAccountDto(userAccountDto.getUsername(),
                    userAccountDto.getPassword(),
                    userAccountDto.getEmail(),
                    "PATIENT"
            );
            //valid name existed in database table user account
            if(authService.existsByUserName(userAccountDto.getUsername())){
                return  ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message","Tên người dùng đã được sử dụng"));
            }
            //valid email existed in database in table patient
            if(patientService.existsPatientByEmail(userAccountDto.getEmail())){
                return  ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message","Email đã được sử dụng"));
            }
            //save user account in database
            authService.registerUser(userAccount);

            //Create 1 record patient in patient table
            PatientDto patientDto = new PatientDto();
            patientDto.setFullName(userAccountDto.getUsername());
            patientDto.setEmail(userAccountDto.getEmail());
            patientService.addPatient(patientDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","Đăng ký thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Đã có lỗi trong quá trình xử lý đăng ký"+e.getMessage()));
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            login_auditService.log(username, "LOGIN_FAILED: USERNAME_NOT_FOUND", request);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Không tìm thấy tên người dùng"));
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            login_auditService.log(username, "LOGIN_FAILED: WRONG_PASSWORD", request);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Mật khẩu không chính xác"));
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            login_auditService.log(username, "LOGIN_SUCCESS", request);
            String jwt = jwtUtil.generateToken((UserDetails) authentication.getPrincipal());
            return ResponseEntity.ok(new LoginResponse(jwt));

        } catch (BadCredentialsException e) {
            login_auditService.log(username, "LOGIN_FAILED: BAD_CREDENTIALS", request);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Thông tin đăng nhập không hợp lệ"));
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

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");

        // 1. Tìm người dùng
        UserAccount user = authService.getByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Email không tồn tại trong hệ thống"));
        }

        // 2. Tạo mật khẩu tạm thời và cập nhật DB
        String tempPassword = PasswordUtil.generateRandomPassword(10);
        String hashed = passwordEncoder.encode(tempPassword);
        user.setPasswordHash(hashed);
        user.setStatus("PENDING"); // bắt buộc đổi mật khẩu sau đó
        userAccountRepository.save(user);

        // 3. Gửi email - xử lý lỗi gửi riêng
        try {
            emailService.send(
                    email,
                    "Khôi phục mật khẩu",
                    "Mật khẩu mới của bạn là: " + tempPassword + "\nVui lòng đăng nhập và đổi mật khẩu ngay."
            );
            return ResponseEntity.ok(Map.of("message","Đã gửi mật khẩu mới đến email của bạn"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Không thể gửi email. Vui lòng thử lại sau."));
        }
    }





}
