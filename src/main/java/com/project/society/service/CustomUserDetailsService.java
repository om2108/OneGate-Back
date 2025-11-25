// src/main/java/com/project/society/service/CustomUserDetailsService.java
package com.project.society.service;

import com.project.society.model.User;
import com.project.society.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // build authorities from either List<String> roles or single Role enum
        Collection<SimpleGrantedAuthority> authorities = buildAuthorities(user);

        boolean enabled = user.isVerified(); // login only allowed if verified (consistent with controller)

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .disabled(!enabled)
                .build();
    }

    private Collection<SimpleGrantedAuthority> buildAuthorities(User user) {
        List<String> roleNames = new ArrayList<>();

        // prefer explicit roles list if present
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            roleNames.addAll(user.getRoles().stream()
                    .filter(r -> r != null && !r.isEmpty())
                    .map(Object::toString)
                    .collect(Collectors.toList()));
        }

        // fallback to single enum Role if roles list empty
        if (roleNames.isEmpty() && user.getRole() != null) {
            roleNames.add(user.getRole().name());
        }

        // if still empty, default to USER
        if (roleNames.isEmpty()) {
            roleNames.add("USER");
        }

        return roleNames.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .collect(Collectors.toList());
    }
}
