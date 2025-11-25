package com.project.society.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="notifications")
public class Notification {

    @Id
    private String id;

    private String message;
    private String targetUserId;
    private String readStatus; // UNREAD, READ

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
