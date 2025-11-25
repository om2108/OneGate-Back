package com.project.society.service;

import com.project.society.model.Notification;
import com.project.society.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository repo;

    public Notification createNotification(Notification n){
        n.setCreatedAt(LocalDateTime.now());
        n.setUpdatedAt(LocalDateTime.now());
        n.setReadStatus("UNREAD");
        return repo.save(n);
    }

    public List<Notification> getNotifications(String userId){
        return repo.findByTargetUserIdAndReadStatus(userId,"UNREAD");
    }

    public Notification markAsRead(String id){
        Notification n = repo.findById(id).orElseThrow(() -> new RuntimeException("Notification not found"));
        n.setReadStatus("READ");
        n.setUpdatedAt(LocalDateTime.now());
        return repo.save(n);
    }
}

