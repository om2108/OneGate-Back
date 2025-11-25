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
@Document(collection="members")
public class Member {
    @Id
    private String id;
    private String userId;
    private String societyId;
    private String role; // OWNER, SECRETARY, WATCHMAN, MEMBER
    private LocalDateTime joinedAt;
}

