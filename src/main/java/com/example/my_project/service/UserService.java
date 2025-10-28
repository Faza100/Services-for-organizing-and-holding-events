package com.example.my_project.service;

import org.springframework.stereotype.Service;

import com.example.my_project.entity.UserEntity;
import com.example.my_project.mapper.UserMapper;
import com.example.my_project.model.User;
import com.example.my_project.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

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
        return userMapper.toModel(userEntity);
    }

    @Transactional
    public User createUser(User user) {
        UserEntity userEntity = userRepository.save(userMapper.toEntity(user));
        return userMapper.toModel(userEntity);
    }

    @Transactional(readOnly = true)
    public User getUserByLogin(String login) {
        UserEntity userEntity = userRepository.getUserByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("User not found with login: "
                        + login));
        return userMapper.toModel(userEntity);
    }

    @Transactional(readOnly = true)
    public boolean existUserByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    @Transactional
    public User registerUser(User newUser) {
        if (existUserByLogin(newUser.getLogin())) {
            throw new IllegalArgumentException("User whith login already exists");
        }
        var hashedPass = passwordEncoder.encode(newUser.getPassword());
        User user = new User(
                newUser.getLogin(),
                newUser.getAge(),
                hashedPass);
        return createUser(user);
    }

}
