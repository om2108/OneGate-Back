package com.project.society.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceResponse {

    private String id;

    private String societyId;

    private String userId;

    private String residentName;

    private String flatNumber;

    // FIXED FIELD
    private Double amount;

    private Integer month;

    private Integer year;

    private LocalDate dueDate;

    private String paymentStatus;
}