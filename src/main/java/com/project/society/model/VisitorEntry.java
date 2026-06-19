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

    private String flatNumber;

    /*
      NEW
      REGULAR
    */
    private String visitorCategory;

    private String visitorType;

    private String createdBy;

    private String approvalLevel;

    private String secretaryDecision;

    private Boolean notifyResident;

    private String residentId;

    private String status;

    private Boolean imageVerified;

    private Boolean active;

    private LocalDateTime checkIn;

    private LocalDateTime checkOut;

    private LocalDateTime approvedAt;

    private LocalDateTime lastVisit;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}