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
@Document(collection="complaints")
public class Complaint {
    @Id
    private String id;

    private String description;       // Complaint description
    private String createdBy;         // User ID of member
    private String societyId;         // Society ID
    private String status;            // PENDING, RESOLVED
    private List<String> assignedTo;  // Secretary or admin assigned
    private String category;          // e.g., Maintenance, Security, Facilities
    private String priority;          // LOW, MEDIUM, HIGH
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

