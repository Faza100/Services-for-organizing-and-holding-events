package com.example.my_project.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.my_project.enums.UserRole;
import com.example.my_project.model.User;

import jakarta.annotation.PostConstruct;

@Service
public class DefaultUserInitializer {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public DefaultUserInitializer(
            UserService userService,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initUsers() {
        createUserIfNotExists("adminn", "adminn", UserRole.ADMIN);
        createUserIfNotExists("userrr", "userrr", UserRole.USER);
    }

    private void createUserIfNotExists(
            String login,
            String password,
            UserRole role) {

        var hashedPass = passwordEncoder.encode(password);

        User user = new User(
                login,
                18,
                hashedPass,
                role);

        userService.createUser(user);
    }

}