package com.project.society.service;

import com.project.society.model.Profile;
import com.project.society.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProfileService {
    private final ProfileRepository repo;

    public ProfileService(ProfileRepository repo) {
        this.repo = repo;
    }

    public Profile getProfile(String email) {
        return repo.findByUserId(email)
                .orElseGet(() -> {
                    Profile p = new Profile();
                    p.setUserId(email);
                    p.setCreatedAt(LocalDateTime.now());
                    repo.save(p);
                    return p;
                });
    }

    public Profile updateProfile(String email, Profile profile) {
        Profile existing = getProfile(email);
        existing.setFullName(profile.getFullName());
        existing.setPhone(profile.getPhone());
        existing.setAddress(profile.getAddress());
        existing.setImage(profile.getImage());
        existing.setUpdatedAt(LocalDateTime.now());
        return repo.save(existing);
    }
}
