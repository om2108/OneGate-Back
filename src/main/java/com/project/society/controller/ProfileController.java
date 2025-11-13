package com.project.society.controller;

import com.project.society.model.Profile;
import com.project.society.service.ProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @GetMapping
    public Profile getProfile(Authentication authentication) {
        String email = authentication.getName(); // ✅ email from JWT
        return service.getProfile(email);
    }

    @PutMapping
    public Profile updateProfile(Authentication authentication, @RequestBody Profile profile) {
        String email = authentication.getName();
        return service.updateProfile(email, profile);
    }
}
