package com.project.society.service;

import com.project.society.model.VisitorEntry;
import com.project.society.repository.VisitorEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VisitorEntryService {

    @Autowired
    private VisitorEntryRepository repo;

    public VisitorEntry addVisitor(VisitorEntry v){
        v.setStatus("PENDING");
        v.setCreatedAt(LocalDateTime.now());
        v.setUpdatedAt(LocalDateTime.now());
        return repo.save(v);
    }

    public List<VisitorEntry> getVisitorEntries(String societyId,List<String> userIds){

        if(userIds==null || userIds.isEmpty()){
            return repo.findBySocietyId(societyId);
        }

        return repo.findBySocietyIdAndNotifiedToIn(societyId,userIds);
    }

    public VisitorEntry updateStatus(String id,String status){

        VisitorEntry v=repo.findById(id)
                .orElseThrow(()->new RuntimeException("Visitor not found"));

        v.setStatus(status);
        v.setUpdatedAt(LocalDateTime.now());

        return repo.save(v);
    }

    public void deleteVisitor(String id){
        repo.deleteById(id);
    }
}
