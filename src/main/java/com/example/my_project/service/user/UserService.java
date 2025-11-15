package com.example.my_project.service.user;

import org.springframework.stereotype.Service;

import com.example.my_project.entity.UserEntity;
import com.example.my_project.mapper.UserMapper;
import com.example.my_project.model.User;
import com.example.my_project.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        log.info("User found with id: {}", userId);
        return userMapper.toModel(userEntity);
    }

    @Transactional
    public User createUser(User user) {
        log.info("Creating new user with login: {}", user.getLogin());
        UserEntity userEntity = userRepository.save(userMapper.toEntity(user));
        log.info("User created successfully with id: {}", userEntity.getId());
        return userMapper.toModel(userEntity);
    }

    @Transactional(readOnly = true)
    public User getUserByLogin(String login) {
        log.info("Getting user by login: {}", login);
        UserEntity userEntity = userRepository.getUserByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("User not found with login: "
                        + login));
        log.info("User found with login: {}", login);
        return userMapper.toModel(userEntity);
    }

    @Transactional(readOnly = true)
    public boolean existUserByLogin(String login) {
        log.debug("Checking if user exists with login: {}", login);
        boolean exists = userRepository.existsByLogin(login);
        log.debug("User exists with login {}: {}", login, exists);
        return exists;
    }

    @Transactional
    public User registerUser(User newUser) {
        log.info("Starting user registration for login: {}", newUser.getLogin());
        if (existUserByLogin(newUser.getLogin())) {
            log.warn("Registration failed - user with login already exists: {}", newUser.getLogin());
            throw new IllegalArgumentException("User whith login already exists");
        }
        var hashedPass = passwordEncoder.encode(newUser.getPassword());
        User user = new User(
                newUser.getLogin(),
                newUser.getAge(),
                hashedPass);
        log.info("User registered successfully with login: {}", newUser.getLogin());
        return createUser(user);
    }

}
