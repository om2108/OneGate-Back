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

    // GET PROFILE (auto-create if missing)
    public Profile getProfile(String userId) {

        final String uid = userId.trim();   // ✅ make final copy

        return repo.findByUserId(uid)
                .orElseGet(() -> {
                    Profile p = new Profile();
                    p.setUserId(uid);
                    p.setCreatedAt(LocalDateTime.now());
                    return repo.save(p);
                });
    }

    // UPDATE PROFILE
    public Profile updateProfile(String userId, Profile profile) {

        final String uid = userId.trim();

        Profile existing = repo.findByUserId(uid)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // Aadhaar uniqueness
        if (profile.getAadhaar() != null && !profile.getAadhaar().isBlank()) {
            if (!profile.getAadhaar().equals(existing.getAadhaar())
                    && repo.existsByAadhaar(profile.getAadhaar())) {
                throw new RuntimeException("Aadhaar already registered");
            }
        }

        // PAN uniqueness
        if (profile.getPan() != null && !profile.getPan().isBlank()) {
            if (!profile.getPan().equals(existing.getPan())
                    && repo.existsByPan(profile.getPan())) {
                throw new RuntimeException("PAN already registered");
            }
        }

        existing.setFullName(profile.getFullName());
        existing.setPhone(profile.getPhone());
        existing.setAddress(profile.getAddress());
        existing.setImage(profile.getImage());
        existing.setAadhaar(profile.getAadhaar());
        existing.setPan(profile.getPan());
        existing.setPassportPhoto(profile.getPassportPhoto());
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }
}
