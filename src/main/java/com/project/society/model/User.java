// src/main/java/com/project/society/model/User.java
package com.project.society.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String email;
    private String password;   // null until onboarding

    private String name;
    private String phone;

    private Role role;                 // OWNER, MEMBER, SECRETARY, WATCHMAN, USER
    private boolean verified = false;  // true after onboarding
    private boolean onboardingCompleted = false;

    private Instant createdAt = Instant.now();
}
