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
    private String type;
    private String status;
    private String location;
    private double price;
    private String image;
    private String societyId;

    private List<String> ownerIds = new ArrayList<>();
    private List<String> residentIds = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer views;
    private Double rating;

    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        if (name != null) sb.append(name);
        if (type != null) sb.append(" - ").append(type);
        if (location != null) sb.append(" (").append(location).append(")");
        return sb.toString();
    }

    // ✅ FIXED
    public String getOwnerId() {
        return ownerIds != null && !ownerIds.isEmpty() ? ownerIds.get(0) : null;
    }

}
