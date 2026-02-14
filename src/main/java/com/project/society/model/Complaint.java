package com.project.society.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "complaints")
public class Complaint {

    @Id
    private String id;

    private String societyId;
    private String createdBy;

    private String title;
    private String description;

    private String category;
    private String priority; // LOW / MEDIUM / HIGH

    private String status; // PENDING / IN_PROGRESS / RESOLVED

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
