package com.project.society.controller;

import com.project.society.model.Notification;
import com.project.society.model.ReadStatus;
import com.project.society.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    // JWT stores EMAIL/USERNAME as principal (String)
    private String getUserId(Authentication authentication) {
        return authentication.getName();   // ⭐ FIX (NO CAST)
    }

    // ================= ALL =================

    @GetMapping
    public List<Notification> all(Authentication authentication) {

        return notificationRepository
                .findByTargetUserIdOrderByCreatedAtDesc(
                        getUserId(authentication)
                );
    }

    // ================= COUNT =================

    @GetMapping("/count")
    public long count(Authentication authentication) {

        return notificationRepository.countByTargetUserIdAndReadStatus(
                getUserId(authentication),
                ReadStatus.UNREAD
        );
    }

    // ================= SINGLE READ =================

    @PutMapping("/{id}/read")
    public void markRead(@PathVariable String id) {

        notificationRepository.findById(id).ifPresent(n -> {
            n.setReadStatus(ReadStatus.READ);
            notificationRepository.save(n);
        });
    }

    // ================= ALL READ =================

    @PutMapping("/read-all")
    public void markAll(Authentication authentication) {

        String userId = getUserId(authentication);

        List<Notification> list =
                notificationRepository
                        .findByTargetUserIdAndReadStatusOrderByCreatedAtDesc(
                                userId,
                                ReadStatus.UNREAD
                        );

        list.forEach(n -> n.setReadStatus(ReadStatus.READ));
        notificationRepository.saveAll(list);
    }
}
