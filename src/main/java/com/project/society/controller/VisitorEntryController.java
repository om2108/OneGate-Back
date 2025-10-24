package com.project.society.controller;

import com.project.society.model.VisitorEntry;
import com.project.society.service.VisitorEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visitors")
public class VisitorEntryController {
    @Autowired
    private VisitorEntryService service;

    @PostMapping
    public VisitorEntry add(@RequestBody VisitorEntry v){ return service.addVisitor(v); }

    @GetMapping
    public List<VisitorEntry> get(@RequestParam String societyId, @RequestParam List<String> userIds){
        return service.getVisitorEntries(societyId, userIds);
    }

    @PutMapping("/{id}/status")
    public VisitorEntry updateStatus(@PathVariable String id,@RequestParam String status){
        return service.updateStatus(id,status);
    }

    @DeleteMapping("/{id}") public void delete(@PathVariable String id){ service.deleteVisitor(id); }
}

