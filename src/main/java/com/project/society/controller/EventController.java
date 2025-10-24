package com.project.society.controller;

import com.project.society.model.EventCalendar;
import com.project.society.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    private EventService service;

    @PostMapping
    public EventCalendar create(@RequestBody EventCalendar e){ return service.createEvent(e); }

    @GetMapping
    public List<EventCalendar> get(@RequestParam String societyId, @RequestParam List<String> roles){
        return service.getEvents(societyId, roles);
    }

    @PutMapping("/{id}") public EventCalendar update(@PathVariable String id,@RequestBody EventCalendar e){ return service.updateEvent(id,e); }

    @DeleteMapping("/{id}") public void delete(@PathVariable String id){ service.deleteEvent(id); }
}

