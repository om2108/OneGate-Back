// src/main/java/com/project/society/dto/RecommendRequest.java
package com.project.society.dto;

public class RecommendRequest {
    private int k = 6;
    private String location;
    private Double minPrice;
    private Double maxPrice;
    private Double alpha; // optional weight for explicit vs implicit

    // getters & setters
    public int getK() { return k; }
    public void setK(int k) { this.k = k; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Double getMinPrice() { return minPrice; }
    public void setMinPrice(Double minPrice) { this.minPrice = minPrice; }

    public Double getMaxPrice() { return maxPrice; }
    public void setMaxPrice(Double maxPrice) { this.maxPrice = maxPrice; }

    public Double getAlpha() { return alpha; }
    public void setAlpha(Double alpha) { this.alpha = alpha; }
}
