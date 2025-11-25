// src/main/java/com/project/society/service/OtpService.java
package com.project.society.service;

import com.project.society.model.OtpToken;
import com.project.society.repository.OtpTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpTokenRepository repo;
    private final EmailService emailService;
    private final SecureRandom random = new SecureRandom();

    // generate and send OTP
    public String generateOtp(String email) {
        String code = String.format("%06d", random.nextInt(1_000_000));
        OtpToken token = new OtpToken();
        token.setEmail(email);
        token.setCode(code);
        token.setExpiresAt(Instant.now().plus(10, ChronoUnit.MINUTES));
        repo.save(token);

        // send email (you already have sendInviteLink; use simple mail)
        emailService.sendOtpCode(email, code); // implement method below
        return code;
    }

    // verify - returns true if code matches and not expired
    public boolean verifyOtp(String email, String code) {
        Optional<OtpToken> opt = repo.findTopByEmailOrderByCreatedAtDesc(email);
        if (opt.isEmpty()) return false;
        OtpToken t = opt.get();
        if (t.getExpiresAt() == null || Instant.now().isAfter(t.getExpiresAt())) return false;
        if (!t.getCode().equals(code)) return false;
        return true;
    }

    // delete tokens for email (cleanup after success)
    public void deleteOtpForEmail(String email) {
        repo.deleteByEmail(email);
    }
}
