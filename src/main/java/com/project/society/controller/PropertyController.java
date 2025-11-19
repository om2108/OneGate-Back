// src/main/java/com/project/society/controller/PropertyController.java
package com.project.society.controller;

import com.project.society.model.Property;
import com.project.society.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyService service;

    // List all properties (admin use)
    @GetMapping
    public List<Property> getAll() {
        return service.getAllProperties();
    }

    // NEW: properties in a specific society (secretary / admin dashboard)
    @GetMapping("/by-society/{societyId}")
    public List<Property> getBySociety(@PathVariable String societyId) {
        return service.getBySociety(societyId);
    }

    // NEW: all properties where user is owner (owner dashboard / admin panel)
    @GetMapping("/by-owner/{userId}")
    public List<Property> getByOwner(@PathVariable String userId) {
        return service.getByOwner(userId);
    }

    // NEW: all properties where user is resident/tenant
    @GetMapping("/by-resident/{userId}")
    public List<Property> getByResident(@PathVariable String userId) {
        return service.getByResident(userId);
    }

    // Create property (admin / society owner)
    @PostMapping
    public Property add(@RequestBody Property p) {
        return service.addProperty(p);
    }

    // Update core property info + owners/residents if passed
    @PutMapping("/{id}")
    public Property update(@PathVariable String id, @RequestBody Property p) {
        return service.updateProperty(id, p);
    }

    // NEW: only change owners of property
    @PutMapping("/{id}/owners")
    public Property updateOwners(
            @PathVariable String id,
            @RequestBody List<String> ownerIds
    ) {
        return service.updateOwners(id, ownerIds);
    }

    // NEW: only change residents/tenants of property
    @PutMapping("/{id}/residents")
    public Property updateResidents(
            @PathVariable String id,
            @RequestBody List<String> residentIds
    ) {
        return service.updateResidents(id, residentIds);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteProperty(id);
    }
}
