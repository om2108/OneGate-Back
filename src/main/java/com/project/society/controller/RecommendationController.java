package com.project.society.controller;

import com.project.society.dto.RecommendRequest;
import com.project.society.model.Property;
import com.project.society.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    /**
     * Recommend endpoint — supports both authenticated (personalized)
     * and anonymous (generic fallback) calls.
     */
    @PostMapping("/recommend")
    public ResponseEntity<List<Property>> recommend(@RequestBody RecommendRequest req,
                                                    Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            List<Property> recs = recommendationService.recommendForUser(email, req);
            return ResponseEntity.ok(recs);
        } else {
            List<Property> recs = recommendationService.recommendGeneric(req);
            return ResponseEntity.ok(recs);
        }
    }
}
