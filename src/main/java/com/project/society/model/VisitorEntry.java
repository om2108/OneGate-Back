package com.project.society.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Document(collection="visitors")

public class VisitorEntry {

    @Id
    private String id;

    private String societyId;

    private String visitorName;

    private String phone;

    private String vehicleNumber;

    private String purpose;

    private String imageUrl;

    private String approvalLevel;

    private String secretaryDecision;

    private String memberDecision;

    private String memberId;

    private String status;

    private Boolean imageVerified;

    private LocalDateTime checkIn;

    private LocalDateTime checkOut;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}