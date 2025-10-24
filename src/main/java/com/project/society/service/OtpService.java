package com.project.society.service;

import com.project.society.model.OtpToken;
import com.project.society.repository.OtpTokenRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {
    private final OtpTokenRepository otpRepo;
    private final EmailService emailService;

    public OtpService(OtpTokenRepository otpRepo, EmailService emailService) {
        this.otpRepo = otpRepo;
        this.emailService = emailService;
    }

    public void generateOtp(String email) {
        String code = String.format("%06d", new Random().nextInt(1_000_000));
        OtpToken token = new OtpToken();
        token.setEmail(email);
        token.setCode(code);
        token.setExpiresAt(Instant.now().plus(10, ChronoUnit.MINUTES));
        otpRepo.save(token);

        emailService.sendOtp(email, code);
    }

    public boolean verifyOtp(String email, String code) {
        Optional<OtpToken> otpOpt = otpRepo.findTopByEmailOrderByCreatedAtDesc(email);
        if (otpOpt.isEmpty()) return false;
        OtpToken otp = otpOpt.get();
        if (otp.getExpiresAt().isBefore(Instant.now())) return false;
        boolean valid = otp.getCode().equals(code);
        if (valid) otpRepo.deleteByEmail(email);
        return valid;
    }
}
