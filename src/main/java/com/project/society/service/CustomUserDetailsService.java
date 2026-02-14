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
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Collection<SimpleGrantedAuthority> authorities = buildAuthorities(user);

        // ✅ RETURN CUSTOM USER (NOT SPRING DEFAULT)
        return new CustomUserDetails(user, authorities);
    }

    private Collection<SimpleGrantedAuthority> buildAuthorities(User user) {

        List<String> roleNames = new ArrayList<>();

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            roleNames.addAll(user.getRoles());
        }

        if (roleNames.isEmpty() && user.getRole() != null) {
            roleNames.add(user.getRole().name());
        }

        if (roleNames.isEmpty()) {
            roleNames.add("USER");
        }

        return roleNames.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .collect(Collectors.toList());
    }
}
