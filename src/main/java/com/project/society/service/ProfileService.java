package com.project.society.service;

import com.project.society.model.Profile;
import com.project.society.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository repo;

    public Profile getProfile(String userId){
        return repo.findByUserId(userId).orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public Profile updateProfile(String userId, Profile updated){
        Profile profile = getProfile(userId);
        profile.setFullName(updated.getFullName());
        profile.setPhone(updated.getPhone());
        profile.setAddress(updated.getAddress());
        profile.setImage(updated.getImage());
        profile.setUpdatedAt(LocalDateTime.now());
        return repo.save(profile);
    }
}

