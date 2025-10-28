package com.example.my_project.service;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JwtTokenFilterService extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private static final Logger log = LoggerFactory.getLogger(JwtTokenFilterService.class);

    public JwtTokenFilterService(
            JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("Authorization header: {}", authorization);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.info("No Bearer token found, skipping authentication");
            filterChain.doFilter(request, response);
            return;
        }

        var jwtToken = authorization.substring(7);

        if (!jwtTokenService.isTokenValid(jwtToken)) {
            log.info(" Jwt token not valid");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            filterChain.doFilter(request, response);
            return;
        }

        var login = jwtTokenService.getLoginFromToken(jwtToken);
        var role = jwtTokenService.getRoleFromToken(jwtToken);
        log.info("User authenticated: {}, role: {}", login, role);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                login, null, List.of(new SimpleGrantedAuthority(role)));
        SecurityContextHolder.getContext().setAuthentication(token);
        filterChain.doFilter(request, response);
    }

}
