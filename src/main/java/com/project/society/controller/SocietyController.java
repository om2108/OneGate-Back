package com.project.society.controller;

import com.project.society.model.Society;
import com.project.society.repository.SocietyRepository;
import com.project.society.service.SocietyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/societies")
public class SocietyController {
    @Autowired
    private SocietyRepository repo;
    @Autowired
    private SocietyService service;

    @GetMapping
    public List<Society> getAll(){ return repo.findAll(); }
    @PostMapping
    public Society add(@RequestBody Society s){
        s.setCreatedAt(LocalDateTime.now());
        s.setUpdatedAt(LocalDateTime.now());
        return repo.save(s);
    }
    @PutMapping("/{id}") public Society update(@PathVariable String id,@RequestBody Society s){
        Society existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        existing.setName(s.getName());
        existing.setAddress(s.getAddress());
        existing.setUpdatedAt(LocalDateTime.now());
        return repo.save(existing);
    }
    @DeleteMapping("/{id}") public void delete(@PathVariable String id){ repo.deleteById(id); }

    // --- NEW API 1: GET MAINTENANCE AMOUNT ---
    @GetMapping("/maintenance")
    public double getMaintenance(@RequestParam String userId) {
        return service.calculateMaintenance(userId);
    }

    // --- NEW API 2: GET SOCIETY FACILITIES ---
    @GetMapping("/facilities")
    public List<String> getFacilities(@RequestParam String societyId) {
        return service.getSocietyFacilities(societyId);
    }
}

