package com.project.society.repository;

import com.project.society.model.VisitorEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VisitorEntryRepository extends MongoRepository<VisitorEntry,String> {
    List<VisitorEntry> findBySocietyIdAndNotifiedToIn(String societyId, List<String> userIds);
}