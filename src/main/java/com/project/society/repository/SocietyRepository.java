// src/main/java/com/project/society/repository/SocietyRepository.java
package com.project.society.repository;

import com.project.society.model.Society;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SocietyRepository extends MongoRepository<Society, String> {

    // ✅ finds societies where ownerIds list contains this ownerId
    List<Society> findByOwnerIdsContains(String ownerId);
}
