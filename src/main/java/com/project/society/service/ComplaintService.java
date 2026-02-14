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

    public Complaint createComplaint(Complaint c){

        c.setStatus("PENDING");
        c.setCreatedAt(LocalDateTime.now());
        c.setUpdatedAt(LocalDateTime.now());

        return repo.save(c);
    }

    public List<Complaint> getComplaintsBySociety(String societyId){
        return repo.findBySocietyId(societyId);
    }

    public List<Complaint> getComplaintsByMember(String userId){
        return repo.findByCreatedBy(userId);
    }

    public Complaint getComplaintById(String id){
        return repo.findById(id)
                .orElseThrow(()->new RuntimeException("Complaint not found"));
    }

    public Complaint updateComplaintStatus(String id,String status,String priority){

        Complaint c=getComplaintById(id);

        c.setStatus(status);

        if(priority!=null){
            c.setPriority(priority);
        }

        c.setUpdatedAt(LocalDateTime.now());

        return repo.save(c);
    }

    public void deleteComplaint(String id){

        Complaint c=getComplaintById(id);

        if(!c.getStatus().equalsIgnoreCase("resolved")){
            throw new RuntimeException("Only resolved complaints can be deleted");
        }

        repo.deleteById(id);
    }
}
