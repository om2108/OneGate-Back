package com.project.society.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // ⭐ NEW — send onboarding invite link
    public void sendInviteLink(String email) {
        String link = "http://localhost:5173/onboarding?email=" + email;

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("OneGate Account Invitation");

        msg.setText(
                "You have been invited to join OneGate.\n\n" +
                        "Click the link below to complete your account setup:\n" +
                        link + "\n\n" +
                        "Set your password and finish onboarding.\n" +
                        "If you didn't expect this email, you can ignore it."
        );

        mailSender.send(msg);
    }
}
