package com.project.society.service;

import com.project.society.model.Property;
import com.project.society.model.Society;
import com.project.society.repository.PropertyRepository;
import com.project.society.repository.SocietyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PropertyService {
    @Autowired
    private PropertyRepository repo;

    public List<Property> getAllProperties(){ return repo.findAll(); }

    public Property addProperty(Property p){
        p.setCreatedAt(LocalDateTime.now());
        p.setUpdatedAt(LocalDateTime.now());
        return repo.save(p);
    }

    public Property updateProperty(String id, Property p){
        Property existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        existing.setName(p.getName());
        existing.setType(p.getType());
        existing.setStatus(p.getStatus());
        existing.setPrice(p.getPrice());
        existing.setLocation(p.getLocation());
        existing.setImage(p.getImage());
        existing.setUpdatedAt(LocalDateTime.now());
        return repo.save(existing);
    }

    public void deleteProperty(String id){ repo.deleteById(id); }
}

