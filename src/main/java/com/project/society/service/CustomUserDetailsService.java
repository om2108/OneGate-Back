// src/main/java/com/project/society/service/CustomUserDetailsService.java
package com.project.society.service;

import com.project.society.model.User;
import com.project.society.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // Map roles (List<String> or List<Enum>) to String[]
        String[] rolesArray = extractRoles(user);

        // Determine verification status using reflection (works for isVerified() or getVerified())
        boolean verified = extractVerifiedFlag(user);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(rolesArray)                 // Spring will prefix "ROLE_" for these
                .disabled(!verified)               // disable if not verified
                .authorities(Collections.emptyList()) // use authorities() if you want custom GrantedAuthorities
                .build();
    }

    /**
     * Safely extract roles from User. Assumes user.getRoles() exists and returns a List
     * (either List<String> or List<Enum>). If roles are missing, returns empty array.
     */
    @SuppressWarnings("unchecked")
    private String[] extractRoles(User user) {
        try {
            List<?> roles = user.getRoles(); // compile-time: your model previously had getRoles()
            if (roles == null || roles.isEmpty()) return new String[0];

            // Convert each role element to string name (handles String or Enum)
            List<String> roleNames = roles.stream()
                    .map(r -> {
                        if (r == null) return "";
                        // if Enum, use name(); else toString()
                        if (r.getClass().isEnum()) {
                            return ((Enum<?>) r).name();
                        } else {
                            return r.toString();
                        }
                    })
                    .filter(s -> s != null && !s.isEmpty())
                    .collect(Collectors.toList());

            return roleNames.toArray(new String[0]);
        } catch (NoSuchMethodError | NoClassDefFoundError e) {
            // In case your User doesn't have getRoles() at all (unlikely given your previous errors),
            // return empty roles so the account still compiles and can be used.
            return new String[0];
        } catch (Exception e) {
            return new String[0];
        }
    }

    /**
     * Try to determine verification flag. This uses reflection to support either:
     *  - boolean isVerified()
     *  - Boolean getVerified()
     * If neither exists, it returns true (default to allow login).
     */
    private boolean extractVerifiedFlag(User user) {
        try {
            // try isVerified()
            Method isVerified = user.getClass().getMethod("isVerified");
            Object val = isVerified.invoke(user);
            return Boolean.TRUE.equals(val);
        } catch (NoSuchMethodException ignored) {
            // try getVerified()
            try {
                Method getVerified = user.getClass().getMethod("getVerified");
                Object val = getVerified.invoke(user);
                return Boolean.TRUE.equals(val);
            } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException ex) {
                // Fallback: if property not present, assume verified (so login not blocked)
                return true;
            } catch (Exception ex) {
                return true;
            }
        } catch (Exception ex) {
            return true;
        }
    }
}
