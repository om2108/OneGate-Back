package com.project.society.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="notices")
public class Notice {
    @Id
    private String id;
    private String title;
    private String description;
    private String createdBy;
    private String societyId;
    private List<String> targetRoles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
