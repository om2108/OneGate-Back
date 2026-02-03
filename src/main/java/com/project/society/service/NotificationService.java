package com.project.society.service;

import com.project.society.dto.NotificationDto;
import com.project.society.model.Notification;
import com.project.society.model.ReadStatus;
import com.project.society.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repo;
    private final NotificationSocketService socket;

    // CREATE + PUSH SOCKET
    public void create(String userId, String message) {

        repo.save(new Notification(
                null,
                message,
                userId,
                ReadStatus.UNREAD,
                LocalDateTime.now(),
                LocalDateTime.now()
        ));

        // realtime push
        socket.push(userId);
    }

    public List<NotificationDto> getAll(String userId) {

        return repo.findByTargetUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(n -> new NotificationDto(
                        n.getId(),
                        n.getMessage(),
                        n.getReadStatus().name(),
                        n.getCreatedAt()
                ))
                .toList();
    }

    public long unreadCount(String userId) {
        return repo.countByTargetUserIdAndReadStatus(userId, ReadStatus.UNREAD);
    }

    public void markOne(String id) {

        Notification n = repo.findById(id)
                .orElseThrow();

        n.setReadStatus(ReadStatus.READ);
        n.setUpdatedAt(LocalDateTime.now());

        repo.save(n);
    }

    public void markAll(String userId) {

        List<Notification> list =
                repo.findByTargetUserIdAndReadStatusOrderByCreatedAtDesc(
                        userId,
                        ReadStatus.UNREAD
                );

        list.forEach(n -> {
            n.setReadStatus(ReadStatus.READ);
            n.setUpdatedAt(LocalDateTime.now());
        });

        repo.saveAll(list);
    }
}
