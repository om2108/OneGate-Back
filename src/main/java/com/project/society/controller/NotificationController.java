package com.project.society.controller;

import com.project.society.model.Notification;
import com.project.society.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepo;

    // ================= GET MY NOTIFICATIONS =================
    @GetMapping
    public List<Notification> getMyNotifications() {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        // 🔥 ALWAYS EMAIL (matches frontend + JWT)
        String email = auth.getName();

        return notificationRepo.findByTargetUserIdOrderByCreatedAtDesc(email);
    }

    // ================= MARK AS READ =================
    @PutMapping("/{id}/read")
    public Notification markRead(@PathVariable String id) {

        Notification n = notificationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        n.setReadStatus("READ");
        n.setUpdatedAt(LocalDateTime.now());

        return notificationRepo.save(n);
    }
}
