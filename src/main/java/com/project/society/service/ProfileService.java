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
        Profile existing = repo.findByUserId(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        if (profile.getAadhaar() != null && !profile.getAadhaar().isEmpty()) {
            if (existing.getAadhaar() == null || !profile.getAadhaar().equals(existing.getAadhaar())) {
                if (repo.existsByAadhaar(profile.getAadhaar())) {
                    throw new RuntimeException("Aadhaar already registered");
                }
            }
        }

        if (profile.getPan() != null && !profile.getPan().isEmpty()) {
            if (existing.getPan() == null || !profile.getPan().equals(existing.getPan())) {
                if (repo.existsByPan(profile.getPan())) {
                    throw new RuntimeException("PAN already registered");
                }
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
