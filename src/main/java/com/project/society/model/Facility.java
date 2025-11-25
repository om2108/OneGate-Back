package com.project.society.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "facilities")
public class Facility {

    @Id
    private String id;
    private String societyId; // Foreign Key to Society
    private String name;
    private String description;
    private String status;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Boolean bookingRequired = false;
    private Double usageChargePerHour;
}