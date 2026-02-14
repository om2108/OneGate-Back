package com.project.society.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    private String id;

    private String message;

    // Mongo USER ID
    private String targetUserId;

    private ReadStatus readStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
