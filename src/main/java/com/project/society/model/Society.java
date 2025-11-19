package com.project.society.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Data @NoArgsConstructor @AllArgsConstructor
@Document(collection="societies")
public class Society {
    @Id private String id;
    private String name;
    private String address;
    private String ownerId;
    private List<String> properties;

    private double monthlyMaintenanceFee;
    private List<String> facilities;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

