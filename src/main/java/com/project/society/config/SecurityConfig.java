package com.project.society.config;

import com.project.society.security.JwtAuthenticationFilter;
import com.project.society.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity  // Enables @PreAuthorize and @Secured
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthenticationFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    // ✅ Password Encoder Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Authentication Manager Bean
    @Bean
    public AuthenticationManager authManager(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new org.springframework.security.authentication.ProviderManager(provider);
    }

    // ✅ Main Security Configuration
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 🔒 Disable CSRF for API
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())

                // 🔒 Stateless session (JWT-based)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 🔑 Authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Public authentication routes
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/auth/verify-otp",
                                "/api/auth/resend-otp"
                        ).permitAll()

                        // 🔓 Allow access to general read endpoints (optional)
                        .requestMatchers("/api/societies", "/api/societies/**").hasAnyRole("OWNER", "SECRETARY","USER")

                        // ✅ Secure all API endpoints (JWT required)
                        .requestMatchers("/api/**").authenticated()

                        // Everything else
                        .anyRequest().authenticated()
                )

                // 🧩 Add JWT filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
