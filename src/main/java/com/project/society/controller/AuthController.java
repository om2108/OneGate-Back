package com.project.society.controller;

import com.project.society.dto.LoginRequest;
import com.project.society.dto.RegisterRequest;
import com.project.society.model.User;
import com.project.society.security.JwtProvider;
import com.project.society.service.OtpService;
import com.project.society.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtProvider jwtProvider;
    private final OtpService otpService;

    public AuthController(UserService userService, AuthenticationManager authManager, JwtProvider jwtProvider, OtpService otpService) {
        this.userService = userService;
        this.authManager = authManager;
        this.jwtProvider = jwtProvider;
        this.otpService = otpService;
    }

    // ----------------- LOGIN -----------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = userService.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!user.isVerified()) {
                return ResponseEntity.badRequest().body(Map.of("error", "User email not verified"));
            }

            String token = jwtProvider.generateToken(user.getEmail(), user.getRole().name());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "email", user.getEmail(),
                    "role", user.getRole()
            ));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(403).body(Map.of("error", "Invalid credentials"));
        }
    }

    // ----------------- REGISTER -----------------
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        if (userService.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already registered"));
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole() != null ? request.getRole() : User.Role.USER);

        User savedUser = userService.register(user);

        // Generate OTP immediately
        otpService.generateOtp(savedUser.getEmail());

        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully. Please verify your email with OTP.",
                "email", savedUser.getEmail(),
                "role", savedUser.getRole()
        ));
    }

    // ----------------- VERIFY OTP -----------------
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        if (email == null || code == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email and code are required"));
        }

        boolean valid = otpService.verifyOtp(email, code);
        if (!valid) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired OTP"));
        }

        userService.markVerified(email);

        return ResponseEntity.ok(Map.of("message", "Email verified successfully"));
    }

    // ----------------- RESEND OTP -----------------
    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || userService.findByEmail(email).isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email not registered"));
        }

        otpService.generateOtp(email);

        return ResponseEntity.ok(Map.of("message", "OTP resent successfully"));
    }
}
