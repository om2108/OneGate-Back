// ============================================
// JwtAuthenticationFilter.java
// UPDATED FINAL
// ============================================

package com.project.society.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request
    ) {

        String path =
                request.getServletPath();

        return path.startsWith("/api/auth")
                || path.startsWith("/api/upload")
                || path.startsWith("/ws");
    }

    @Override
    protected void doFilterInternal(

            HttpServletRequest request,

            HttpServletResponse response,

            FilterChain filterChain

    ) throws ServletException, IOException {

        try {

            String token =
                    resolveToken(request);

            if (StringUtils.hasText(token)) {

                Jws<Claims> claimsJws =

                        jwtProvider.validateToken(
                                token
                        );

                Claims claims =
                        claimsJws.getBody();

                String email =
                        claims.getSubject();

                String userId =
                        claims.get(
                                "id",
                                String.class
                        );

                String role =
                        claims.get(
                                "role",
                                String.class
                        );

                // =====================================
                // FIX ROLE PREFIX
                // =====================================

                if (

                        role != null

                                &&

                                !role.startsWith(
                                        "ROLE_"
                                )

                ) {

                    role =
                            "ROLE_" + role;
                }

                List<SimpleGrantedAuthority>
                        authorities =

                        List.of(

                                new SimpleGrantedAuthority(
                                        role
                                )
                        );

                UsernamePasswordAuthenticationToken
                        auth =

                        new UsernamePasswordAuthenticationToken(

                                userId,

                                null,

                                authorities
                        );

                auth.setDetails(email);

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(auth);
            }

        } catch (Exception ex) {

            SecurityContextHolder.clearContext();

            logger.error(
                    "JWT ERROR: " +
                            ex.getMessage()
            );
        }

        filterChain.doFilter(
                request,
                response
        );
    }

    private String resolveToken(
            HttpServletRequest req
    ) {

        String bearer =
                req.getHeader(
                        "Authorization"
                );

        if (

                StringUtils.hasText(
                        bearer
                )

                        &&

                        bearer.startsWith(
                                "Bearer "
                        )
        ) {

            return bearer.substring(7);
        }

        return null;
    }
}