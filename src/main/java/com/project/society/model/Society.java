// src/main/java/com/project/society/model/Society.java
package com.project.society.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "societies")
public class Society {

    @Id
    private String id;

    private String name;
    private String address;

    // CHANGED: single ownerId -> list of ownerIds (multi-owner society)
    private List<String> ownerIds = new ArrayList<>();

    // still keep list of property ids owned/managed by this society
    private List<String> properties = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
