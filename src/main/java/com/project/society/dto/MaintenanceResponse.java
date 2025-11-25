package com.project.society.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceResponse {

    private String societyId;
    private String userId;
    private Double totalMaintenanceAmount; // The simple final amount requested
    private LocalDate dueDate;

    // Optional: Keep breakdown fields if you need them later, but API returns only the total
    // private Double baseFee;
    // private Double areaBasedCharge;
    // ...
}