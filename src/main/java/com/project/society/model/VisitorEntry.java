package com.project.society.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "visitors")
public class VisitorEntry {

    @Id
    private String id;

    private String societyId;

    private String visitorName;
    private String phone;

    private String imageUrl;

    private String notifiedTo; // userId

    private String status; // PENDING / APPROVED / REJECTED

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
