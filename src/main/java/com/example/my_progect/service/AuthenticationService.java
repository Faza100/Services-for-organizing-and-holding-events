package com.example.my_progect.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.my_progect.dto.user.LoginRequestDto;
import com.example.my_progect.model.User;

import jakarta.transaction.Transactional;

@Service
public class AuthenticationService {

    private final JwtTokenService jwtTokenService;

    public final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(
            JwtTokenService jwtTokenService,
            PasswordEncoder passwordEncoder,
            UserService userService) {
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Transactional
    public String authenticateUser(LoginRequestDto request) {
        if (!userService.existUserByLogin(request.getLogin())) {
            throw new IllegalArgumentException("Invalid login");
        }

        User user = userService.getUserByLogin(request.getLogin());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return jwtTokenService.generateToken(user);
    }

}
