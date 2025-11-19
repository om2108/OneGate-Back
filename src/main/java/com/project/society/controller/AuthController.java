// src/main/java/com/project/society/controller/AuthController.java
package com.project.society.controller;

import com.project.society.dto.LoginRequest;
import com.project.society.dto.RegisterRequest;
import com.project.society.model.User;
import com.project.society.security.JwtProvider;
import com.project.society.service.OtpService;
import com.project.society.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtProvider jwtProvider;
    private final OtpService otpService;

    // 1) Register user & send OTP
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {

        if (userService.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.status(409).body(Map.of("error", "Email already registered"));
        }

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setRole(req.getRole());
        user.setVerified(false);

        User saved = userService.register(user);
        otpService.generateOtp(req.getEmail());

        return ResponseEntity.created(URI.create("/api/users/" + saved.getId()))
                .body(Map.of(
                        "message", "OTP sent to email",
                        "userId", saved.getId()
                ));
    }

    // 2) Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String code = payload.get("code");
        if (email == null || code == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "email and code required"));
        }

        boolean verified = otpService.verifyOtp(email, code);
        if (!verified) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired OTP"));
        }

        userService.markVerified(email);
        return ResponseEntity.ok(Map.of("message", "Email verified successfully"));
    }

    // 3) Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        var optUser = userService.findByEmail(request.getEmail());
        if (optUser.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
        User user = optUser.get();

        if (!user.isVerified()) {
            return ResponseEntity.status(403).body(Map.of("error", "Email not verified"));
        }

        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

            String token = jwtProvider.generateToken(user.getEmail(), user.getRole().name());
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "email", user.getEmail(),
                    "role", user.getRole().name()
            ));
        } catch (Exception ex) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }

    // 4) Me
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }
        String email = authentication.getName();
        return userService.findByEmail(email)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "User not found")));
    }
}
