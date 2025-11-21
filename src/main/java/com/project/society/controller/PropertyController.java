package com.project.society.controller;

import com.project.society.model.Property;
import com.project.society.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService service;

    @GetMapping
    public List<Property> getAll() {
        return service.getAllProperties();
    }

    @GetMapping("/by-society/{societyId}")
    public List<Property> getBySociety(@PathVariable String societyId) {
        return service.getBySociety(societyId);
    }

    @GetMapping("/by-owner/{userId}")
    public List<Property> getByOwner(@PathVariable String userId) {
        return service.getByOwner(userId);
    }

    @GetMapping("/by-resident/{userId}")
    public List<Property> getByResident(@PathVariable String userId) {
        return service.getByResident(userId);
    }

    @PostMapping
    public Property add(@RequestBody Property p) {
        return service.addProperty(p);
    }

    @PutMapping("/{id}")
    public Property update(@PathVariable String id, @RequestBody Property p) {
        return service.updateProperty(id, p);
    }

    @PutMapping("/{id}/owners")
    public Property updateOwners(@PathVariable String id, @RequestBody List<String> ownerIds) {
        return service.updateOwners(id, ownerIds);
    }

    @PutMapping("/{id}/residents")
    public Property updateResidents(@PathVariable String id, @RequestBody List<String> residentIds) {
        return service.updateResidents(id, residentIds);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteProperty(id);
    }
}
