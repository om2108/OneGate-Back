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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .cors(cors -> {
                })
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ⭐⭐⭐ WEBSOCKET FIX
                        .requestMatchers("/ws/**").permitAll()

                        // Auth / public
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/users/onboarding").permitAll()
                        .requestMatchers("/api/users/public").permitAll()
                        .requestMatchers("/api/upload/**").permitAll()

                        // Users
                        .requestMatchers("/api/users/invite").hasRole("OWNER")
                        .requestMatchers("/api/users/**").authenticated()

                        // Societies
                        .requestMatchers(HttpMethod.GET, "/api/societies/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/societies/**").hasAnyRole("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/societies/**").hasAnyRole("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/societies/**").hasAnyRole("ADMIN", "OWNER")

                        // Members
                        .requestMatchers("/api/members/**")
                        .hasAnyRole("ADMIN", "SECRETARY", "WATCHMAN")

                        // Complaints
                        .requestMatchers(HttpMethod.GET, "/api/complaints/society/**").hasAnyRole("SECRETARY", "OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/complaints/member/**").hasAnyRole("MEMBER", "USER", "SECRETARY", "OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/complaints/*").hasAnyRole("MEMBER", "USER", "SECRETARY", "OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/complaints/**").hasAnyRole("MEMBER", "USER", "SECRETARY", "OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/complaints/**").hasAnyRole("SECRETARY", "OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/complaints/**").hasAnyRole("SECRETARY", "OWNER", "ADMIN")

                        // Visitors
                        .requestMatchers(HttpMethod.GET, "/api/visitors/**")
                        .hasAnyRole("WATCHMAN", "SECRETARY", "ADMIN", "OWNER")

                        .requestMatchers(HttpMethod.POST, "/api/visitors/**")
                        .hasAnyRole("WATCHMAN", "SECRETARY", "ADMIN", "OWNER")

                        .requestMatchers(HttpMethod.PUT, "/api/visitors/**")
                        .hasAnyRole("WATCHMAN", "SECRETARY", "ADMIN", "OWNER")

                        .requestMatchers(HttpMethod.DELETE, "/api/visitors/**")
                        .hasAnyRole("WATCHMAN", "SECRETARY", "ADMIN", "OWNER")

                        // Properties
                        .requestMatchers(HttpMethod.POST, "/api/properties/recommend").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/properties/events/property-click").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/properties/**").hasAnyRole("ADMIN", "OWNER", "SECRETARY", "MEMBER", "WATCHMAN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/properties/**").hasAnyRole("ADMIN", "OWNER", "SECRETARY")
                        .requestMatchers(HttpMethod.PUT, "/api/properties/**").hasAnyRole("ADMIN", "OWNER", "SECRETARY")
                        .requestMatchers(HttpMethod.DELETE, "/api/properties/**").hasAnyRole("ADMIN", "OWNER")

                        // Notices
                        .requestMatchers(HttpMethod.GET, "/api/notices/**").hasAnyRole("ADMIN", "OWNER", "SECRETARY", "MEMBER", "WATCHMAN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/notices/**").hasAnyRole("ADMIN", "SECRETARY", "OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/notices/**").hasAnyRole("ADMIN", "SECRETARY", "OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/notices/**").hasAnyRole("ADMIN", "SECRETARY", "OWNER")

                        // Events
                        .requestMatchers(HttpMethod.GET, "/api/events/**").hasAnyRole("ADMIN", "OWNER", "SECRETARY", "MEMBER", "WATCHMAN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/events/**").hasAnyRole("ADMIN", "OWNER", "SECRETARY")
                        .requestMatchers(HttpMethod.PUT, "/api/events/**").hasAnyRole("ADMIN", "OWNER", "SECRETARY")
                        .requestMatchers(HttpMethod.DELETE, "/api/events/**").hasAnyRole("ADMIN", "OWNER", "SECRETARY")

                        .requestMatchers("/api/profile/**").authenticated()
                        .requestMatchers("/api/no-show/**").authenticated()
                        .requestMatchers("/api/notifications/**")
                        .hasAnyRole("WATCHMAN", "SECRETARY", "ADMIN", "OWNER", "USER", "MEMBER")


                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}