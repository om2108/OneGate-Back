// src/main/java/com/project/society/service/ComplaintService.java
package com.project.society.service;

import com.project.society.model.Complaint;
import com.project.society.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository repo;

    // Member adds complaint
    public Complaint createComplaint(Complaint c) {
        c.setStatus("PENDING");
        c.setCreatedAt(LocalDateTime.now());
        c.setUpdatedAt(LocalDateTime.now());
        return repo.save(c);
    }

    // Secretary or owner sees all complaints of society
    public List<Complaint> getComplaintsBySociety(String societyId) {
        return repo.findBySocietyId(societyId);
    }

    // Member sees only their complaints
    public List<Complaint> getComplaintsByMember(String userId) {
        return repo.findByCreatedBy(userId);
    }

    // Get single complaint by id (for getComplaintById API)
    public Complaint getComplaintById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
    }

    // Secretary updates status and priority
    public Complaint updateComplaintStatus(String id, String status, String priority) {
        Complaint c = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        c.setStatus(status);
        if (priority != null) {
            c.setPriority(priority);
        }
        c.setUpdatedAt(LocalDateTime.now());
        return repo.save(c);
    }

    // Delete complaint only if resolved
    public void deleteComplaint(String id) {
        Complaint c = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        if (!"RESOLVED".equals(c.getStatus())) {
            throw new RuntimeException("Only resolved complaints can be deleted");
        }
        repo.deleteById(id);
    }
}
