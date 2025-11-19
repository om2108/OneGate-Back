// src/main/java/com/project/society/repository/UserRepository.java
package com.project.society.repository;

import com.project.society.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
