package com.project.society.service;

import com.project.society.model.User;
import com.project.society.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;

    private final PasswordEncoder encoder;

    // =====================================
    // REGISTER
    // =====================================

    public User register(User user) {

        // ENCODE PASSWORD

        if (user.getPassword() != null) {

            user.setPassword(

                    encoder.encode(
                            user.getPassword()
                    )
            );
        }

        // FIX ROLE LIST

        if (user.getRoles() == null) {

            user.setRoles(
                    new ArrayList<>()
            );
        }

        if (

                user.getRoles().isEmpty()

                        &&

                        user.getRole() != null

        ) {

            user.getRoles().add(

                    "ROLE_" +

                            user.getRole().name()
            );
        }

        return repo.save(user);
    }

    // =====================================
    // PASSWORD ENCODE
    // =====================================

    public String encodePassword(
            String rawPassword
    ) {

        return encoder.encode(
                rawPassword
        );
    }

    // =====================================
    // FIND EMAIL
    // =====================================

    public Optional<User> findByEmail(
            String email
    ) {

        return repo.findByEmail(email);
    }

    // =====================================
    // FIND ID
    // =====================================

    public Optional<User> findById(
            String id
    ) {

        return repo.findById(id);
    }

    // =====================================
    // VERIFY USER
    // =====================================

    public void markVerified(
            String email
    ) {

        repo.findByEmail(email)

                .ifPresent(u -> {

                    u.setVerified(true);

                    // FIX OLD USERS

                    if (u.getRoles() == null) {

                        u.setRoles(
                                new ArrayList<>()
                        );
                    }

                    if (

                            u.getRoles().isEmpty()

                                    &&

                                    u.getRole() != null

                    ) {

                        u.getRoles().add(

                                "ROLE_" +

                                        u.getRole().name()
                        );
                    }

                    repo.save(u);
                });
    }

    // =====================================
    // SAVE
    // =====================================

    public User save(
            User user
    ) {

        return repo.save(user);
    }
}