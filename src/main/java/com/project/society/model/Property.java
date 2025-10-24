package com.project.society.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
@Document(collection="properties")
public class Property {
    @Id private String id;
    private String name;
    private String type;
    private String status;
    private String location;
    private double price;
    private String image;
    private String societyId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

