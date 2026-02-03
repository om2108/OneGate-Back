package com.project.society.repository;

import com.project.society.model.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface ProfileRepository extends MongoRepository<Profile, String> {
    Optional<Profile> findByUserId(String userId);

    boolean existsByAadhaar(String aadhaar);
    boolean existsByPan(String pan);
}
