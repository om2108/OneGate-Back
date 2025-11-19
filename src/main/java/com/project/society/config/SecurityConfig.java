// src/main/java/com/project/society/config/SecurityConfig.java
package com.project.society.config;

import com.project.society.security.JwtAuthenticationFilter;
import com.project.society.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtFilter;

    // -----------------------------------
    // BEANS
    // -----------------------------------
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    // -----------------------------------
    // MAIN SECURITY CONFIG
    // -----------------------------------
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // ⭐ Allow CORS preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ⭐ PUBLIC ENDPOINTS
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/users/onboarding").permitAll()
                        .requestMatchers("/api/users/public").permitAll()
                        .requestMatchers("/api/upload/**").permitAll()

                        // ⭐ USER MANAGEMENT
                        .requestMatchers("/api/users/invite").hasRole("OWNER")
                        .requestMatchers("/api/users/**").authenticated()

                        // ⭐ SOCIETIES
                        .requestMatchers(HttpMethod.GET, "/api/societies/**")
                        .hasAnyRole("ADMIN", "OWNER", "SECRETARY", "MEMBER", "WATCHMAN")
                        .requestMatchers(HttpMethod.POST, "/api/societies/**")
                        .hasAnyRole("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/societies/**")
                        .hasAnyRole("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/societies/**")
                        .hasAnyRole("ADMIN", "OWNER")

                        // ⭐ APPOINTMENTS
                        .requestMatchers(HttpMethod.GET, "/api/appointments/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/appointments/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/appointments/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/appointments/**").authenticated()

                        // ⭐ MEMBERS (Secretary + Admin)
                        .requestMatchers("/api/members/**").hasAnyRole("SECRETARY", "ADMIN")

                                // ⭐ COMPLAINTS

// Secretary / Owner / Admin: can view all complaints in a society
                                .requestMatchers(HttpMethod.GET, "/api/complaints/society/**")
                                .hasAnyRole("SECRETARY", "OWNER", "ADMIN")

// Any logged-in user (including MEMBER) can see their own complaints
                                .requestMatchers(HttpMethod.GET, "/api/complaints/member/**")
                                .hasAnyRole("MEMBER", "USER", "SECRETARY", "OWNER", "ADMIN")

// Any logged-in user can get single complaint by id
                                .requestMatchers(HttpMethod.GET, "/api/complaints/*")
                                .hasAnyRole("MEMBER", "USER", "SECRETARY", "OWNER", "ADMIN")

// ✅ MEMBER (and any logged-in user) can create complaints
                                .requestMatchers(HttpMethod.POST, "/api/complaints/**")
                                .hasAnyRole("MEMBER", "USER", "SECRETARY", "OWNER", "ADMIN")

// Only secretary / owner / admin can update status & priority
                                .requestMatchers(HttpMethod.PUT, "/api/complaints/**")
                                .hasAnyRole("SECRETARY", "OWNER", "ADMIN")

// Only secretary / owner / admin can delete (and service restricts to RESOLVED)
                                .requestMatchers(HttpMethod.DELETE, "/api/complaints/**")
                                .hasAnyRole("SECRETARY", "OWNER", "ADMIN")


                                // ⭐ VISITORS (Guard, Secretary, Admin)
                        .requestMatchers(HttpMethod.GET, "/api/visitors/**")
                        .hasAnyRole("GUARD", "SECRETARY", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/visitors/**")
                        .hasAnyRole("GUARD", "SECRETARY", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/visitors/**")
                        .hasAnyRole("GUARD", "SECRETARY", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/visitors/**")
                        .hasAnyRole("GUARD", "SECRETARY", "ADMIN")

                        // ⭐ PROPERTIES (Multi-owner, residents, admin)
                        .requestMatchers(HttpMethod.GET, "/api/properties/**")
                        .hasAnyRole("ADMIN", "OWNER", "SECRETARY", "MEMBER", "WATCHMAN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/properties/**")
                        .hasAnyRole("ADMIN", "OWNER", "SECRETARY")
                        .requestMatchers(HttpMethod.PUT, "/api/properties/**")
                        .hasAnyRole("ADMIN", "OWNER", "SECRETARY")
                        .requestMatchers(HttpMethod.DELETE, "/api/properties/**")
                        .hasAnyRole("ADMIN", "OWNER")

                                // ⭐ NOTICES
// Everyone who is logged in can see notices
                                .requestMatchers(HttpMethod.GET, "/api/notices/**")
                                .hasAnyRole("ADMIN", "OWNER", "SECRETARY", "MEMBER", "WATCHMAN", "USER")
// Only secretary/admin (and optionally owner) can create/update/delete
                                .requestMatchers(HttpMethod.POST, "/api/notices/**")
                                .hasAnyRole("ADMIN", "SECRETARY", "OWNER")
                                .requestMatchers(HttpMethod.PUT, "/api/notices/**")
                                .hasAnyRole("ADMIN", "SECRETARY", "OWNER")
                                .requestMatchers(HttpMethod.DELETE, "/api/notices/**")
                                .hasAnyRole("ADMIN", "SECRETARY", "OWNER")


                                // ⭐ PROFILE
                        .requestMatchers("/api/profile/**").authenticated()

                        // ⭐ ALL OTHER ENDPOINTS REQUIRE LOGIN
                        .anyRequest().authenticated()
                )

                // Add JWT filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
