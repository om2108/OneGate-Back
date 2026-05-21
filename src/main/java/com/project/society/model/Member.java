// src/main/java/com/project/society/model/Member.java

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
@Document(collection = "members")
public class Member {

    @Id
    private String id;

    private String userId;

    private String societyId;

    // ✅ IMPORTANT
    private String propertyId;

    // OWNER, SECRETARY, WATCHMAN, MEMBER
    private String role;

    private LocalDateTime joinedAt;
}