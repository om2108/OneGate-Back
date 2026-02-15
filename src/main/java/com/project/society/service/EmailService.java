package com.project.society.service;

import com.resend.Resend;
import com.resend.services.emails.model.SendEmailRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${app.email.apikey}")
    private String apiKey;

    @Value("${app.email.from}")
    private String from;

    private Resend resend;

    @PostConstruct
    public void init() {
        resend = new Resend(apiKey);
    }

    public void sendOtpCode(String email, String code) {

        SendEmailRequest request = SendEmailRequest.builder()
                .from(from)
                .to(email)
                .subject("OneGate — Your verification code")
                .html("<h2>Your OTP: <b>" + code + "</b></h2>"
                        + "<p>This code expires in 10 minutes.</p>")
                .build();

        resend.emails().send(request);
    }

    public void sendInviteLink(String email) {

        String link = "https://onegate.onrender.com/onboarding?email=" + email;

        SendEmailRequest request = SendEmailRequest.builder()
                .from(from)
                .to(email)
                .subject("OneGate Account Invitation")
                .html("<h2>You are invited to OneGate 🎉</h2>"
                        + "<p>Click below:</p>"
                        + "<a href='" + link + "'>Complete Setup</a>")
                .build();

        resend.emails().send(request);
    }
}
