// src/main/java/com/project/society/controller/ComplaintController.java
package com.project.society.controller;

import com.project.society.model.Complaint;
import com.project.society.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService service;

    // Member adds complaint
    @PostMapping
    public Complaint create(@RequestBody Complaint c) {
        return service.createComplaint(c);
    }

    // Secretary or owner sees all complaints in society
    @GetMapping("/society")
    public List<Complaint> getBySociety(@RequestParam String societyId) {
        return service.getComplaintsBySociety(societyId);
    }

    // Member sees only their complaints
    @GetMapping("/member")
    public List<Complaint> getByMember(@RequestParam String userId) {
        return service.getComplaintsByMember(userId);
    }

    // Get complaint by id (for getComplaintById API)
    @GetMapping("/{id}")
    public Complaint getOne(@PathVariable String id) {
        return service.getComplaintById(id);
    }

    // Secretary updates status and priority
    @PutMapping("/{id}/status")
    public Complaint updateStatus(
            @PathVariable String id,
            @RequestParam String status,
            @RequestParam(required = false) String priority
    ) {
        return service.updateComplaintStatus(id, status, priority);
    }

    // Delete complaint if resolved
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteComplaint(id);
    }
}
