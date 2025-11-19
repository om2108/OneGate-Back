// src/main/java/com/project/society/model/Property.java
package com.project.society.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "properties")
public class Property {

    @Id
    private String id;

    private String name;
    private String type;      // you already have PropertyType enum if you want to use it
    private String status;    // you already have PropertyStatus enum if you want to use it
    private String location;
    private double price;
    private String image;

    // already there: which society this property belongs to
    private String societyId;

    // NEW: support multiple owners for a property
    private List<String> ownerIds = new ArrayList<>();

    // NEW: support multiple residents/tenants for a property
    private List<String> residentIds = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
