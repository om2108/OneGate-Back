package com.project.society.repository;

import com.project.society.model.Society;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SocietyRepository extends MongoRepository<Society,String> {
    // Optional: direct method if you want
    List<Society> findByOwnerId(String ownerId);
}
