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
@Document(collection="visitor_entries")
public class VisitorEntry {
    @Id
    private String id;
    private String visitorName;
    private String photo;
    private LocalDateTime time;
    private String societyId;
    private List<String> notifiedTo;
    private String status; // PENDING, APPROVED, DISAPPROVED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
