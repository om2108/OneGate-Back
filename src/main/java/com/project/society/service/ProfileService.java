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

    private boolean isProfileComplete(Profile p) {
        return p.getFullName() != null && !p.getFullName().isEmpty() &&
                p.getPhone() != null && !p.getPhone().isEmpty() &&
                p.getAddress() != null && !p.getAddress().isEmpty() &&
                p.getImage() != null && !p.getImage().isEmpty() &&
                p.getAadhaar() != null && !p.getAadhaar().isEmpty() &&
                p.getPan() != null && !p.getPan().isEmpty() &&
                p.getPassportPhoto() != null && !p.getPassportPhoto().isEmpty();
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



        if (profile.getFullName() != null)
            existing.setFullName(profile.getFullName());

        if (profile.getPhone() != null)
            existing.setPhone(profile.getPhone());

        if (profile.getAddress() != null)
            existing.setAddress(profile.getAddress());

        if (profile.getImage() != null)
            existing.setImage(profile.getImage());

        if (profile.getAadhaar() != null)
            existing.setAadhaar(profile.getAadhaar());

        if (profile.getPan() != null)
            existing.setPan(profile.getPan());

        if (profile.getPassportPhoto() != null)
            existing.setPassportPhoto(profile.getPassportPhoto());

        existing.setProfileComplete(isProfileComplete(existing));
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    public Profile getProfile(String email) {

        Profile profile = repo.findByUserId(email)
                .orElseGet(() -> {
                    Profile p = new Profile();
                    p.setUserId(email);
                    p.setCreatedAt(LocalDateTime.now());
                    return repo.save(p);
                });


        boolean complete = isProfileComplete(profile);
        profile.setProfileComplete(complete);

        return repo.save(profile);
    }



}
