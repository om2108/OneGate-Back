package com.project.society.controller;

import com.project.society.service.PropertyEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Lightweight controller to accept property events (click/view)
 * so backend can update popularity / user vectors.
 */
@RestController
@RequestMapping("/api/properties/events")
@RequiredArgsConstructor
public class PropertyEventsController {

    private final PropertyEventService propertyEventService;

    @PostMapping("/property-click")
    public ResponseEntity<?> recordPropertyClick(@RequestBody Map<String, String> body) {
        String propertyId = body.get("propertyId");
        if (propertyId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "propertyId required"));
        }
        propertyEventService.recordClick(propertyId);
        return ResponseEntity.ok(Map.of("ok", true));
    }
}
