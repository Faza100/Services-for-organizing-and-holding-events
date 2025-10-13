package com.example.my_progect.service;

import org.springframework.stereotype.Service;

import com.example.my_progect.entity.UserEntity;
import com.example.my_progect.mapper.UserMapper;
import com.example.my_progect.model.User;
import com.example.my_progect.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
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

    @Transactional
    public User getUserByLogin(String login) {
        UserEntity userEntity = userRepository.getUserByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("User not found with login: "
                        + login));
        return userMapper.toModel(userEntity);
    }

    @Transactional
    public boolean existUserByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

}
