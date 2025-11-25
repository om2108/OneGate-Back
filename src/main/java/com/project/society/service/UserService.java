// src/main/java/com/project/society/service/UserService.java
package com.project.society.service;

import com.project.society.model.User;
import com.project.society.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public User register(User user) {
        if (user.getPassword() != null) {
            user.setPassword(encoder.encode(user.getPassword()));
        }
        // ensure roles list is populated
        if ((user.getRoles() == null || user.getRoles().isEmpty()) && user.getRole() != null) {
            user.getRoles().add(user.getRole().name());
        }
        return repo.save(user);
    }

    public String encodePassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public Optional<User> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    public Optional<User> findById(String id) {
        return repo.findById(id);
    }

    public void markVerified(String email) {
        repo.findByEmail(email).ifPresent(u -> {
            u.setVerified(true);
            repo.save(u);
        });
    }

    // helper used by reset-password
    public User save(User user) {
        return repo.save(user);
    }
}
