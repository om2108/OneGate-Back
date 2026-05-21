// ============================================
// CustomUserDetailsService.java
// UPDATED FINAL
// ============================================

package com.project.society.service;

import com.project.society.model.User;
import com.project.society.repository.UserRepository;
import com.project.society.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(
            String email
    ) throws UsernameNotFoundException {

        User user =
                repo.findByEmail(email)

                        .orElseThrow(() ->

                                new UsernameNotFoundException(
                                        "User not found"
                                )
                        );

        Collection<SimpleGrantedAuthority>
                authorities =
                buildAuthorities(user);

        // =====================================
        // RETURN CUSTOM USER
        // =====================================

        return new CustomUserDetails(
                user,
                authorities
        );
    }

    // =====================================
    // BUILD AUTHORITIES
    // =====================================

    private Collection<SimpleGrantedAuthority>
    buildAuthorities(User user) {

        List<String> roleNames =
                new ArrayList<>();

        // =====================================
        // GET EXISTING ROLES
        // =====================================

        if (

                user.getRoles() != null

                        &&

                        !user.getRoles().isEmpty()

        ) {

            roleNames.addAll(
                    user.getRoles()
            );
        }

        // =====================================
        // FALLBACK SINGLE ROLE
        // =====================================

        if (

                roleNames.isEmpty()

                        &&

                        user.getRole() != null

        ) {

            String role =
                    user.getRole().name();

            // FIX ROLE PREFIX

            if (

                    !role.startsWith(
                            "ROLE_"
                    )

            ) {

                role =
                        "ROLE_" + role;
            }

            roleNames.add(role);
        }

        // =====================================
        // DEFAULT USER
        // =====================================

        if (roleNames.isEmpty()) {

            roleNames.add(
                    "ROLE_USER"
            );
        }

        // =====================================
        // FIX ALL PREFIXES
        // =====================================

        return roleNames.stream()

                .map(role -> {

                    if (

                            !role.startsWith(
                                    "ROLE_"
                            )

                    ) {

                        role =
                                "ROLE_" + role;
                    }

                    return new SimpleGrantedAuthority(
                            role
                    );
                })

                .collect(Collectors.toList());
    }
}