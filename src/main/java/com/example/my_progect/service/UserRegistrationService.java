package com.example.my_progect.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.my_progect.mapper.UserMapper;
import com.example.my_progect.model.User;

@Service
public class UserRegistrationService {

    public final UserService userService;
    public final UserMapper userMapper;
    public final PasswordEncoder passwordEncoder;

    public UserRegistrationService(
            UserService userService,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User newUser) {
        if (userService.existUserByLogin(newUser.getLogin())) {
            throw new IllegalArgumentException("User whith login already exists");
        }
        var hashedPass = passwordEncoder.encode(newUser.getPassword());
        User user = new User(
                newUser.getLogin(),
                newUser.getAge(),
                hashedPass);
        return userService.createUser(user);
    }

}
