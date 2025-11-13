package com.project.society.controller;

import com.project.society.dto.LoginRequest;
import com.project.society.dto.RegisterRequest;
import com.project.society.model.User;
import com.project.society.security.JwtProvider;
import com.project.society.service.OtpService;
import com.project.society.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173") // ✅ allow frontend
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtProvider jwtProvider;
    private final OtpService otpService;

    public AuthController(UserService userService, AuthenticationManager authManager,
                          JwtProvider jwtProvider, OtpService otpService) {
        this.userService = userService;
        this.authManager = authManager;
        this.jwtProvider = jwtProvider;
        this.otpService = otpService;
    }

    // ✅ 1️⃣ Register user & send OTP
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setRole(req.getRole());
        user.setVerified(false);

        userService.register(user);
        otpService.generateOtp(req.getEmail());

        return ResponseEntity.ok(Map.of("message", "OTP sent to email"));
    }

    // ✅ 2️⃣ Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String code = payload.get("code");

        boolean verified = otpService.verifyOtp(email, code);
        if (!verified) {
            return ResponseEntity.status(400).body(Map.of("error", "Invalid or expired OTP"));
        }

        userService.markVerified(email);
        return ResponseEntity.ok(Map.of("message", "Email verified successfully"));
    }

    // ✅ 3️⃣ Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isVerified()) {
            return ResponseEntity.status(400).body(Map.of("error", "Email not verified"));
        }

        String token = jwtProvider.generateToken(user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", user.getEmail(),
                "role", user.getRole()
        ));
    }
}
