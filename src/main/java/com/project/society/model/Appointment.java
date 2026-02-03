package com.project.society.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "appointments")
public class Appointment {

    @Id
    private String id;

    private String propertyId;
    private String userId;

    private String status;
    private String ownerResponse;

    private LocalDateTime dateTime = LocalDateTime.now();
    private String location;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    private Integer pastNoShowsCount = 0;
    private Double percentCancellations = 0.0;
    private String intent = "";
    private String propertyType = "";
    private Double noShowScore = 0.0;
    private Boolean noShowFlag = false;
    private Date lastScoredAt;

    // ✅ FIXED
    public String getTenantId() {
        return this.userId;
    }
}
