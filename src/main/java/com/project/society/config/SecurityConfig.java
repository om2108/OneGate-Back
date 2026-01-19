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
                .cors(cors -> {})
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // Allow CORS preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/users/onboarding").permitAll()
                        .requestMatchers("/api/users/public").permitAll()
                        .requestMatchers("/api/upload/**").permitAll()

                        // User management
                        .requestMatchers("/api/users/invite").hasRole("OWNER")
                        .requestMatchers("/api/users/**").authenticated()

                        // Societies - allow GET publicly
                        .requestMatchers(HttpMethod.GET, "/api/societies/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/societies/**").hasAnyRole("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/societies/**").hasAnyRole("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/societies/**").hasAnyRole("ADMIN", "OWNER")

                        // Appointments
                        .requestMatchers(HttpMethod.GET, "/api/appointments/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/appointments/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/appointments/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/appointments/**").authenticated()

                        // Members
                        .requestMatchers("/api/members/**").hasAnyRole("SECRETARY", "ADMIN")

                        // Complaints
                        .requestMatchers(HttpMethod.GET, "/api/complaints/society/**").hasAnyRole("SECRETARY", "OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/complaints/member/**").hasAnyRole("MEMBER", "USER", "SECRETARY", "OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/complaints/*").hasAnyRole("MEMBER", "USER", "SECRETARY", "OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/complaints/**").hasAnyRole("MEMBER", "USER", "SECRETARY", "OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/complaints/**").hasAnyRole("SECRETARY", "OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/complaints/**").hasAnyRole("SECRETARY", "OWNER", "ADMIN")

                        // Visitors (Guard, Secretary, Admin)
                        .requestMatchers(HttpMethod.GET, "/api/visitors/**").hasAnyRole("WATCHMAN", "SECRETARY", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/visitors/**").hasAnyRole("WATCHMAN", "SECRETARY", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/visitors/**").hasAnyRole("WATCHMAN", "SECRETARY", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/visitors/**").hasAnyRole("WATCHMAN", "SECRETARY", "ADMIN")

                        // Properties: allow anonymous POST recommend endpoint, others guarded
                        .requestMatchers(HttpMethod.POST, "/api/properties/recommend").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/properties/**").hasAnyRole("ADMIN", "OWNER", "SECRETARY", "MEMBER", "WATCHMAN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/properties/**").hasAnyRole("ADMIN", "OWNER", "SECRETARY")
                        .requestMatchers(HttpMethod.PUT, "/api/properties/**").hasAnyRole("ADMIN", "OWNER", "SECRETARY")
                        .requestMatchers(HttpMethod.DELETE, "/api/properties/**").hasAnyRole("ADMIN", "OWNER")

                        // Notices
                        .requestMatchers(HttpMethod.GET, "/api/notices/**").hasAnyRole("ADMIN", "OWNER", "SECRETARY", "MEMBER", "WATCHMAN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/notices/**").hasAnyRole("ADMIN", "SECRETARY", "OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/notices/**").hasAnyRole("ADMIN", "SECRETARY", "OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/notices/**").hasAnyRole("ADMIN", "SECRETARY", "OWNER")

                        // Profile
                        .requestMatchers("/api/profile/**").authenticated()

                        // Events
                        .requestMatchers(HttpMethod.GET, "/api/events/**")
                        .hasAnyRole("ADMIN", "OWNER", "SECRETARY", "MEMBER", "WATCHMAN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/events/**")
                        .hasAnyRole("ADMIN", "OWNER", "SECRETARY")
                        .requestMatchers(HttpMethod.PUT, "/api/events/**")
                        .hasAnyRole("ADMIN", "OWNER", "SECRETARY")
                        .requestMatchers(HttpMethod.DELETE, "/api/events/**")
                        .hasAnyRole("ADMIN", "OWNER", "SECRETARY")


                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
