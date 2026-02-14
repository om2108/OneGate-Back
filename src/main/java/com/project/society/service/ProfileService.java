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

    // ✅ PROFILE COMPLETION CHECK
    private boolean isProfileComplete(Profile p) {
        return p.getFullName() != null && !p.getFullName().isBlank() &&
                p.getPhone() != null && !p.getPhone().isBlank() &&
                p.getAddress() != null && !p.getAddress().isBlank() &&
                p.getImage() != null && !p.getImage().isBlank() &&
                p.getAadhaar() != null && !p.getAadhaar().isBlank() &&
                p.getPan() != null && !p.getPan().isBlank() &&
                p.getPassportPhoto() != null && !p.getPassportPhoto().isBlank();
    }

    // ✅ GET PROFILE (auto-create if missing)
    public Profile getProfile(String userId) {

        final String uid = userId.trim();

        Profile profile = repo.findByUserId(uid)
                .orElseGet(() -> {
                    Profile p = new Profile();
                    p.setUserId(uid);
                    p.setCreatedAt(LocalDateTime.now());
                    return repo.save(p);
                });

        profile.setProfileComplete(isProfileComplete(profile));
        return repo.save(profile);
    }

    // ✅ UPDATE PROFILE (partial update supported)
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
            existing.setAadhaar(profile.getAadhaar());
        }

        // PAN uniqueness
        if (profile.getPan() != null && !profile.getPan().isBlank()) {
            if (!profile.getPan().equals(existing.getPan())
                    && repo.existsByPan(profile.getPan())) {
                throw new RuntimeException("PAN already registered");
            }
            existing.setPan(profile.getPan());
        }

        // Partial updates (only if non-null)
        if (profile.getFullName() != null)
            existing.setFullName(profile.getFullName());

        if (profile.getPhone() != null)
            existing.setPhone(profile.getPhone());

        if (profile.getAddress() != null)
            existing.setAddress(profile.getAddress());

        if (profile.getImage() != null)
            existing.setImage(profile.getImage());

        if (profile.getPassportPhoto() != null)
            existing.setPassportPhoto(profile.getPassportPhoto());

        // Recalculate completion
        existing.setProfileComplete(isProfileComplete(existing));
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }
}
