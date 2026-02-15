package com.project.society.controller;

import com.project.society.dto.LoginRequest;
import com.project.society.dto.RegisterRequest;
import com.project.society.model.User;
import com.project.society.security.JwtProvider;
import com.project.society.service.EmailService;
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
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtProvider jwtProvider;
    private final OtpService otpService;
    private final EmailService emailService;

    // ✅ 1) Register user & send OTP
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {

        if (userService.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.status(409)
                    .body(Map.of("error", "Email already registered"));
        }

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setRole(req.getRole());
        user.setVerified(false);

        // Ensure roles list consistency
        user.getRoles().clear();
        if (req.getRole() != null) {
            user.getRoles().add(req.getRole().name());
        } else {
            user.getRoles().add("USER");
            user.setRole(com.project.society.model.Role.USER);
        }

        User saved = userService.register(user);

        // Generate OTP
        otpService.generateOtp(saved.getEmail());

        return ResponseEntity.created(URI.create("/api/users/" + saved.getId()))
                .body(Map.of(
                        "message", "OTP sent to email",
                        "userId", saved.getId()
                ));
    }

    // ✅ 2) Verify OTP (supports verify/reset)
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> payload) {

        String email = payload.get("email");
        String code = payload.get("code");
        String purpose = payload.get("purpose"); // optional

        if (email == null || code == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "email and code required"));
        }

        boolean verified = otpService.verifyOtp(email, code);
        if (!verified) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid or expired OTP"));
        }

        otpService.deleteOtpForEmail(email);

        if (!"reset".equalsIgnoreCase(purpose)) {
            userService.markVerified(email);
            return ResponseEntity.ok(Map.of("message", "Email verified successfully"));
        }

        return ResponseEntity.ok(Map.of("message", "Code verified for password reset"));
    }

    // ✅ Resend OTP
    @PostMapping("/resend")
    public ResponseEntity<?> resendOtp(@RequestBody Map<String, String> payload) {

        String email = payload.get("email");
        if (email == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "email required"));
        }

        var userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", "User not found"));
        }

        otpService.generateOtp(email);

        return ResponseEntity.ok(Map.of("message", "OTP resent"));
    }

    // ✅ 3a) Forgot password
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> payload) {

        String email = payload.get("email");
        if (email == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "email required"));
        }

        userService.findByEmail(email)
                .ifPresent(user -> otpService.generateOtp(email));

        return ResponseEntity.ok(Map.of(
                "message", "If the email exists, a reset code has been sent."
        ));
    }

    // ✅ 3b) Reset password
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {

        String email = payload.get("email");
        String code = payload.get("code");
        String newPassword = payload.get("password");

        if (email == null || code == null || newPassword == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "email, code and password required"));
        }

        boolean ok = otpService.verifyOtp(email, code);
        if (!ok) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid or expired code"));
        }

        userService.findByEmail(email).ifPresent(user -> {
            user.setPassword(userService.encodePassword(newPassword));
            userService.save(user);
            otpService.deleteOtpForEmail(email);
        });

        return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
    }

    // ✅ 4) Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        var optUser = userService.findByEmail(request.getEmail());
        if (optUser.isEmpty()) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Invalid credentials"));
        }

        User user = optUser.get();

        if (!user.isVerified()) {
            return ResponseEntity.status(403)
                    .body(Map.of("error", "Email not verified"));
        }

        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

            boolean remember = Boolean.TRUE.equals(request.getRememberMe());

            String token = jwtProvider.generateToken(
                    user.getId(),
                    user.getEmail(),
                    user.getRole() != null
                            ? user.getRole().name()
                            : user.getRoles().get(0),
                    remember
            );

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "email", user.getEmail(),
                    "role", user.getRole() != null
                            ? user.getRole().name()
                            : user.getRoles().get(0)
            ));

        } catch (Exception ex) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Invalid credentials"));
        }
    }

    // ✅ 5) Me
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Not authenticated"));
        }

        String email = authentication.getName();

        return userService.findByEmail(email)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.status(404)
                                .body(Map.of("error", "User not found"))
                );
    }
}
