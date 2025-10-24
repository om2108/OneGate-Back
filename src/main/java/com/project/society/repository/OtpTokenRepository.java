package com.project.society.repository;

import com.project.society.model.OtpToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface OtpTokenRepository extends MongoRepository<OtpToken, String> {
    Optional<OtpToken> findTopByEmailOrderByCreatedAtDesc(String email);
    void deleteByEmail(String email);
}
