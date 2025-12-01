package com.project.society.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "appointments")
public class Appointment {

    @Id
    private String id;

    private String propertyId;
    private String userId;

    private String status;           // REQUESTED, ACCEPTED, DECLINED
    private String ownerResponse;

    private LocalDateTime dateTime;
    private String location;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ---------------------------
    // 🔥 ADD THESE FIELDS (required for scoring)
    // ---------------------------

    private Integer pastNoShowsCount;        // e.g., how many times user did not show
    private Double percentCancellations;     // e.g., 0.0 to 1.0
    private String intent;                   // "rent" or "buy"
    private String propertyType;             // "studio", "2bhk", etc.

    private Double noShowScore;              // prediction result
    private Boolean noShowFlag;              // p > threshold?
    private java.util.Date lastScoredAt;     // when score last calculated
}
