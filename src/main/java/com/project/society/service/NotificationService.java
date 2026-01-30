package com.project.society.service;

import com.project.society.model.Notification;
import com.project.society.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repo;

    public NotificationService(NotificationRepository repo) {
        this.repo = repo;
    }

    // Create notification
    public Notification createNotification(Notification n) {
        n.setCreatedAt(LocalDateTime.now());
        n.setUpdatedAt(LocalDateTime.now());
        n.setReadStatus("UNREAD");
        return repo.save(n);
    }

    // Get UNREAD notifications (latest first)
    public List<Notification> getNotifications(String userId) {
        return repo.findByTargetUserIdAndReadStatusOrderByCreatedAtDesc(
                userId,
                "UNREAD"
        );
    }

    // Mark as read
    public Notification markAsRead(String id) {
        Notification n = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        n.setReadStatus("READ");
        n.setUpdatedAt(LocalDateTime.now());
        return repo.save(n);
    }
}
