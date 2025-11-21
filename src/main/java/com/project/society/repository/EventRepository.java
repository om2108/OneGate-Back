package com.project.society.repository;

import com.project.society.model.EventCalendar;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRepository extends MongoRepository<EventCalendar,String> {
    List<EventCalendar> findBySocietyIdAndTargetRolesIn(String societyId, List<String> roles);
}
