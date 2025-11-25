package com.project.society.controller;

import com.project.society.model.Society;
import com.project.society.repository.SocietyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.project.society.model.Facility;
import com.project.society.dto.MaintenanceResponse;
import com.project.society.service.SocietyService;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/societies")
public class SocietyController {
    @Autowired
    private SocietyRepository repo;

    @Autowired // <-- ADD THIS
    private SocietyService societyService;

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

    // 7️⃣ API 1: Get Facilities by Society ID
    @GetMapping("/{societyId}/facilities")
    public List<Facility> getFacilities(@PathVariable String societyId) {
        return societyService.getFacilitiesBySocietyId(societyId);
    }

    // 8️⃣ API 2: Calculate Maintenance (Returns total amount in DTO)
    @GetMapping("/{societyId}/maintenance")
    public ResponseEntity<MaintenanceResponse> calculateMaintenance(
            @PathVariable String societyId,
            @RequestParam String userId)
    {
        try {

            MaintenanceResponse maintenance = societyService.calculateMaintenance(societyId, userId);
            return ResponseEntity.ok(maintenance);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

