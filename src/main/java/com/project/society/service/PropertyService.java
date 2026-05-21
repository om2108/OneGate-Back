// src/main/java/com/project/society/service/PropertyService.java
package com.project.society.service;

import com.project.society.model.Property;
import com.project.society.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository repo;

    public List<Property> getAllProperties() {
        return repo.findAll();
    }

    public Property addProperty(Property p) {
        p.setCreatedAt(LocalDateTime.now());
        p.setUpdatedAt(LocalDateTime.now());
        // ownerIds/residentIds & societyId come from request body
        return repo.save(p);
    }

    public Property updateProperty(String id, Property p) {
        Property existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found: " + id));

        existing.setName(p.getName());
        existing.setType(p.getType());
        existing.setStatus(p.getStatus());
        existing.setPrice(p.getPrice());
        existing.setLocation(p.getLocation());
        existing.setImage(p.getImage());
        existing.setSocietyId(p.getSocietyId());

        // keep multi-owner & multi-resident updated
        if (p.getOwnerIds() != null) {
            existing.setOwnerIds(p.getOwnerIds());
        }

        if (p.getResidentIds() != null) {
            existing.setResidentIds(p.getResidentIds());
        }

        existing.setUpdatedAt(LocalDateTime.now());
        return repo.save(existing);
    }

    public void deleteProperty(String id) {
        repo.deleteById(id);
    }

    // ---------- NEW helper methods for admin panel / dashboards ----------

    public List<Property> getBySociety(String societyId) {
        return repo.findBySocietyId(societyId);
    }

    /**
     * Return all properties where ownerUserId appears in ownerIds list.
     * Note: repository method must be `findByOwnerIdsContaining`.
     */
    public List<Property> getByOwner(String ownerUserId) {
        return repo.findByOwnerIdsContaining(ownerUserId);
    }

    /**
     * Return all properties where residentUserId appears in residentIds list.
     * Note: repository method must be `findByResidentIdsContaining`.
     */
    public List<Property> getByResident(String residentUserId) {
        return repo.findByResidentIdsContaining(residentUserId);
    }

    public Property updateOwners(String propertyId, List<String> ownerIds) {
        Property existing = repo.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found: " + propertyId));

        List<String> current = existing.getOwnerIds();

        if (current == null) current = new ArrayList<>();

        for (String id : ownerIds) {
            if (!current.contains(id)) {
                current.add(id);
            }
        }

        existing.setOwnerIds(current);
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    public Property updateResidents(String propertyId, List<String> residentIds) {
        Property existing = repo.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found: " + propertyId));

        List<String> current = existing.getResidentIds();

        if (current == null) current = new ArrayList<>();

        for (String id : residentIds) {
            if (!current.contains(id)) {
                current.add(id);
            }
        }

        existing.setResidentIds(current);
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    public Property removeResident(String propertyId, String userId) {
        Property existing = repo.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        List<String> residents = existing.getResidentIds();

        if (residents != null) {
            residents.remove(userId);
        }

        existing.setUpdatedAt(LocalDateTime.now());
        return repo.save(existing);
    }

    public Property removeOwner(String propertyId, String userId) {
        Property existing = repo.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        List<String> owners = existing.getOwnerIds();

        if (owners != null) {
            owners.remove(userId);
        }

        existing.setUpdatedAt(LocalDateTime.now());
        return repo.save(existing);
    }
}
