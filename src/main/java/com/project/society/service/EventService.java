package com.project.society.service;

import com.project.society.model.EventCalendar;
import com.project.society.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventRepository repo;

    public EventCalendar createEvent(EventCalendar e){
        e.setCreatedAt(LocalDateTime.now());
        e.setUpdatedAt(LocalDateTime.now());
        return repo.save(e);
    }

    public List<EventCalendar> getEvents(String societyId, List<String> roles){
        return repo.findBySocietyIdAndTargetRolesIn(societyId, roles);
    }

    public EventCalendar updateEvent(String id, EventCalendar updated){
        EventCalendar e = repo.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        e.setTitle(updated.getTitle());
        e.setDescription(updated.getDescription());
        e.setDateTime(updated.getDateTime());
        e.setTargetRoles(updated.getTargetRoles());
        e.setUpdatedAt(LocalDateTime.now());
        return repo.save(e);
    }

    public void deleteEvent(String id){ repo.deleteById(id); }
}

