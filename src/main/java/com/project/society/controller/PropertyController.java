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

    @GetMapping
    public List<Property> getAll(){ return service.getAllProperties(); }
    @PostMapping
    public Property add(@RequestBody Property p){ return service.addProperty(p); }
    @PutMapping("/{id}") public Property update(@PathVariable String id,@RequestBody Property p){ return service.updateProperty(id,p); }
    @DeleteMapping("/{id}") public void delete(@PathVariable String id){ service.deleteProperty(id); }
}
