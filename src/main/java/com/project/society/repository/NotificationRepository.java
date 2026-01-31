package com.project.society.repository;

import com.project.society.model.Notification;
import com.project.society.model.ReadStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findByTargetUserIdOrderByCreatedAtDesc(String userId);

    List<Notification> findByTargetUserIdAndReadStatusOrderByCreatedAtDesc(
            String userId,
            ReadStatus status
    );

    long countByTargetUserIdAndReadStatus(String userId, ReadStatus status);
}
