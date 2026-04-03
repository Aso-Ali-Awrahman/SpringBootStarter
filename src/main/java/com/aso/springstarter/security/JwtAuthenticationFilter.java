package com.aso.springstarter.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // invalid
            filterChain.doFilter(request, response);
            return;
        }

        final var jwt = jwtService.checkAndParseToken(authHeader.substring(7));
        if (jwt == null || (!jwt.getType().isAccessToken() || jwt.isExpired())) {
            // invalid token
            filterChain.doFilter(request, response);
            return;
        }
        // valid token
        final var userPrinciple = jwt.getUser();
        final var authentication = new UsernamePasswordAuthenticationToken(
            userPrinciple, // authentication principal
            null,
            userPrinciple.getAuthorities()
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        final var path = request.getRequestURI();
        return path.startsWith("/public/") || path.contains("swagger");
    }

}
