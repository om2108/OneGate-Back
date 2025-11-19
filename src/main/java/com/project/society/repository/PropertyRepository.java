// src/main/java/com/project/society/repository/PropertyRepository.java
package com.project.society.repository;

import com.project.society.model.Property;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PropertyRepository extends MongoRepository<Property, String> {

    List<Property> findBySocietyId(String societyId);

    List<Property> findByStatus(String status);

    // NEW: all properties where this user is one of the owners
    List<Property> findByOwnerIdsContains(String ownerId);

    // NEW: all properties where this user is one of the residents
    List<Property> findByResidentIdsContains(String residentId);
}
