package com.project.society.controller;

import com.project.society.model.Notification;
import com.project.society.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService service;

    @PostMapping
    public Notification create(@RequestBody Notification n){ return service.createNotification(n); }

    @GetMapping
    public List<Notification> get(@RequestParam String userId){ return service.getNotifications(userId); }

    @PutMapping("/{id}/read") public Notification markRead(@PathVariable String id){ return service.markAsRead(id); }
}
