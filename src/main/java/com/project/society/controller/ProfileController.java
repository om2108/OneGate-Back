package com.project.society.controller;

import com.project.society.model.Profile;
import com.project.society.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    @Autowired
    private ProfileService service;

    @GetMapping
    public Profile getProfile(@RequestHeader("X-User-Id") String userId){
        return service.getProfile(userId);
    }

    @PutMapping
    public Profile updateProfile(@RequestHeader("X-User-Id") String userId,
                                 @RequestBody Profile profile){
        return service.updateProfile(userId, profile);
    }
}
