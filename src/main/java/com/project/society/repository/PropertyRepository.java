package com.project.society.repository;

import com.project.society.model.Property;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PropertyRepository extends MongoRepository<Property,String> {
    List<Property> findBySocietyId(String societyId);
    List<Property> findByStatus(String status);
}

