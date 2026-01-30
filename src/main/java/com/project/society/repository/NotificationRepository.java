package com.project.society.repository;

import com.project.society.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    // All notifications for user (latest first)
    List<Notification> findByTargetUserIdOrderByCreatedAtDesc(String targetUserId);

    // Only UNREAD notifications (for bell badge)
    List<Notification> findByTargetUserIdAndReadStatusOrderByCreatedAtDesc(
            String targetUserId,
            String readStatus
    );

    // Count unread (optional – future use)
    long countByTargetUserIdAndReadStatus(String targetUserId, String readStatus);
}
