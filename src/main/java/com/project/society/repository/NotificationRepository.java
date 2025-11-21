package com.project.society.repository;

import com.project.society.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification,String> {
    List<Notification> findByTargetUserIdAndReadStatus(String targetUserId, String readStatus);
}
