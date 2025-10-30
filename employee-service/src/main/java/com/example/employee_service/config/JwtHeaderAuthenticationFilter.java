package com.example.employee_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtHeaderAuthenticationFilter extends OncePerRequestFilter {

    // These are the header names the API Gateway will add
    public static final String HEADER_USER_EMAIL = "X-User-Email";
    public static final String HEADER_USER_ROLES = "X-User-Roles";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String userEmail = request.getHeader(HEADER_USER_EMAIL);
        String userRoles = request.getHeader(HEADER_USER_ROLES);

        if (userEmail == null || userEmail.isEmpty() || userRoles == null || userRoles.isEmpty()) {
            // If headers are missing, just continue the filter chain.
            // Spring Security will block the request if it's a secured endpoint.
            filterChain.doFilter(request, response);
            return;
        }

        // Headers are present. Create the Authentication object.
        // The roles header is a comma-separated string, e.g., "ROLE_ADMIN,ROLE_USER"
        List<GrantedAuthority> authorities = Arrays.stream(userRoles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Create an authentication token.
        // We use the email as the 'principal' and 'credentials' are null
        // because this token is pre-authenticated (by the gateway).
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userEmail, null, authorities);

        // Set the authentication in the Spring Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Continue to the next filter
        filterChain.doFilter(request, response);
    }
}