package com.project.society.controller;

import com.project.society.model.Profile;
import com.project.society.service.ProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @GetMapping
    public Profile getProfile(Authentication authentication) {

        String userId = authentication.getName(); // JWT userId
        return service.getProfile(userId);
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(Authentication authentication,
                                           @RequestBody Profile profile) {
        try {
            String userId = authentication.getName();
            Profile updated = service.updateProfile(userId, profile);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
