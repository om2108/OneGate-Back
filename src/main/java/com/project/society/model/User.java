package com.project.society.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String name;
    private String email;
    private String password;

    // MULTIPLE roles for Spring Security (String list)
    private List<String> roles = new ArrayList<>();

    // SINGLE role used inside controllers (Enum)
    private Role role;

    // Additional fields
    private String phone;
    private boolean onboardingCompleted = false;
    private boolean verified = false;

    // Recommendation vector
    private double[] vector;
}
