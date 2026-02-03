package com.project.society.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotificationDto {

    private String id;
    private String message;
    private String readStatus;
    private LocalDateTime createdAt;
}
