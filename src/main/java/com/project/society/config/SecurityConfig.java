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
import org.springframework.security.config.Customizer;
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

    // =========================================
    // PASSWORD ENCODER
    // =========================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // =========================================
    // AUTH MANAGER
    // =========================================

    @Bean
    public AuthenticationManager authManager() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(
                userDetailsService
        );

        provider.setPasswordEncoder(
                passwordEncoder()
        );

        return new ProviderManager(
                provider
        );
    }

    // =========================================
    // SECURITY FILTER CHAIN
    // =========================================

    @Bean
    public SecurityFilterChain
    filterChain(HttpSecurity http)
            throws Exception {

        http

                // DISABLE CSRF
                .csrf(csrf ->
                        csrf.disable()
                )

                // ENABLE CORS
                .cors(Customizer.withDefaults())

                // STATELESS SESSION
                .sessionManagement(sm ->

                        sm.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // =========================================
                // AUTHORIZATION
                // =========================================

                .authorizeHttpRequests(auth -> auth

                        // =====================================
                        // OPTIONS
                        // =====================================

                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        // =====================================
                        // WEBSOCKET
                        // =====================================

                        .requestMatchers(
                                "/ws/**"
                        ).permitAll()

                        // =====================================
                        // AUTH
                        // =====================================

                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                                // =====================================
// MAINTENANCE
// =====================================

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/maintenance/**"
                                )
                                .hasAnyRole(
                                        "MEMBER",
                                        "OWNER",
                                        "ADMIN",
                                        "SECRETARY"
                                )

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/maintenance/**"
                                )
                                .hasAnyRole(
                                        "MEMBER",
                                        "OWNER",
                                        "ADMIN",
                                        "SECRETARY"
                                )

                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/maintenance/**"
                                )
                                .hasAnyRole(
                                        "ADMIN",
                                        "SECRETARY",
                                        "OWNER"
                                )

                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/maintenance/**"
                                )
                                .hasAnyRole(
                                        "ADMIN",
                                        "OWNER"
                                )

                        // =====================================
                        // PUBLIC APIs
                        // =====================================

                        .requestMatchers(
                                "/api/users/onboarding"
                        ).permitAll()

                        .requestMatchers(
                                "/api/users/public"
                        ).permitAll()

                        .requestMatchers(
                                "/api/upload/**"
                        ).permitAll()

                        // =====================================
                        // USERS
                        // =====================================

                        .requestMatchers(
                                "/api/users/invite"
                        )
                        .hasAnyRole(
                                "OWNER",
                                "ADMIN"
                        )

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/users/**"
                        )
                        .permitAll()

                        .requestMatchers(
                                "/api/users/**"
                        )
                        .authenticated()

                        // =====================================
                        // SOCIETIES
                        // =====================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/societies/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/societies/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "OWNER"
                        )

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/societies/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "OWNER"
                        )

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/societies/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "OWNER"
                        )

                        // =====================================
                        // MEMBERS
                        // =====================================

                        .requestMatchers(
                                "/api/members/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "SECRETARY",
                                "WATCHMAN",
                                "MEMBER",
                                "OWNER"
                        )

                        // =====================================
                        // COMPLAINTS
                        // =====================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/complaints/society/**"
                        )
                        .hasAnyRole(
                                "SECRETARY",
                                "OWNER",
                                "ADMIN"
                        )

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/complaints/member/**"
                        )
                        .hasAnyRole(
                                "MEMBER",
                                "USER",
                                "SECRETARY",
                                "OWNER",
                                "ADMIN"
                        )

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/complaints/*"
                        )
                        .hasAnyRole(
                                "MEMBER",
                                "USER",
                                "SECRETARY",
                                "OWNER",
                                "ADMIN"
                        )

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/complaints/**"
                        )
                        .hasAnyRole(
                                "MEMBER",
                                "USER",
                                "SECRETARY",
                                "OWNER",
                                "ADMIN"
                        )

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/complaints/**"
                        )
                        .hasAnyRole(
                                "SECRETARY",
                                "OWNER",
                                "ADMIN"
                        )

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/complaints/**"
                        )
                        .hasAnyRole(
                                "SECRETARY",
                                "OWNER",
                                "ADMIN"
                        )

                                // =====================================
// VISITORS
// =====================================

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/visitors/**"
                                )
                                .hasAnyRole(
                                        "WATCHMAN",
                                        "SECRETARY",
                                        "ADMIN",
                                        "OWNER",
                                        "MEMBER"
                                )

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/visitors/**"
                                )
                                .hasAnyRole(
                                        "WATCHMAN",
                                        "SECRETARY",
                                        "ADMIN",
                                        "OWNER",
                                        "MEMBER"
                                )

                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/visitors/**"
                                )
                                .hasAnyRole(
                                        "WATCHMAN",
                                        "SECRETARY",
                                        "ADMIN",
                                        "OWNER"
                                )

                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/visitors/**"
                                )
                                .hasAnyRole(
                                        "SECRETARY",
                                        "ADMIN",
                                        "OWNER"
                                )

                        // =====================================
                        // PROPERTIES
                        // =====================================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/properties/recommend"
                        )
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/properties/events/property-click"
                        )
                        .authenticated()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/properties/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "OWNER",
                                "SECRETARY",
                                "MEMBER",
                                "WATCHMAN",
                                "USER"
                        )

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/properties/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "OWNER",
                                "SECRETARY"
                        )

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/properties/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "OWNER",
                                "SECRETARY"
                        )

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/properties/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "OWNER"
                        )

                        // =====================================
                        // NOTICES
                        // =====================================

                        .requestMatchers(
                                "/api/notices/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "OWNER",
                                "SECRETARY",
                                "MEMBER",
                                "WATCHMAN",
                                "USER"
                        )

                        // =====================================
                        // EVENTS
                        // =====================================

                        .requestMatchers(
                                "/api/events/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "OWNER",
                                "SECRETARY",
                                "MEMBER",
                                "WATCHMAN",
                                "USER"
                        )

                        // =====================================
                        // PROFILE
                        // =====================================

                        .requestMatchers(
                                "/api/profile/**"
                        )
                        .authenticated()

                                // =====================================
// NOTIFICATIONS
// =====================================

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/notifications/send"
                                )
                                .authenticated()

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/notifications/**"
                                )
                                .authenticated()

                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/notifications/**"
                                )
                                .authenticated()

                        // =====================================
                        // ALL OTHER REQUESTS
                        // =====================================

                        .anyRequest()
                        .authenticated()
                )

                // =========================================
                // JWT FILTER
                // =========================================

                .addFilterBefore(

                        jwtFilter,

                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}