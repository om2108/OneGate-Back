package com.project.society.dto;

import lombok.Data;

/**
 * Minimal recommend request DTO.
 */
@Data
public class RecommendRequest {
    private int k = 6;
    private String location;
    private Double minPrice;
    private Double maxPrice;
    // add other fields (alpha, userVector, etc.) if you need
}
