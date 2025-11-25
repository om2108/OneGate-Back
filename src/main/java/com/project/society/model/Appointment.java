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
@Document(collection="appointments")
public class Appointment {
    @Id
    private String id;
    private String propertyId;
    private String userId;
    private String status; // REQUESTED, ACCEPTED, DECLINED
    private String ownerResponse;
    private LocalDateTime dateTime;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer pastNoShowsCount;
    private Double percentCancellations;
    private String intent;
    private String propertyType;

    private Double noShowScore;
    private Boolean noShowFlag;
    private Date lastScoredAt;
}

