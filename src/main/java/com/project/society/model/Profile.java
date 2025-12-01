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
@Document(collection="profiles")
public class Profile {
    @Id
    private String id;
    private String userId;
    private String fullName;
    private String phone;
    private String address;
    private String image;
    private String aadhaar;
    private String pan;
    private String aadhaarStatus;
    private String panStatus;
    private String passportPhoto;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}