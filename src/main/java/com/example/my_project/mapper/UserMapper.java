package com.example.my_project.mapper;

import org.springframework.stereotype.Component;

import com.example.my_project.dto.user.UserloginResponseDto;
import com.example.my_project.entity.UserEntity;
import com.example.my_project.model.User;
import com.example.my_project.dto.user.UserRequestDto;
import com.example.my_project.dto.user.UserResponseDto;

@Component
public class UserMapper {

    public User toModel(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getLogin(),
                userEntity.getAge(),
                userEntity.getPassword(),
                userEntity.getRole());
    }

    public User toModel(UserRequestDto userRequestDto) {
        return new User(
                userRequestDto.getLogin(),
                userRequestDto.getAge(),
                userRequestDto.getPassword());
    }

    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.getLogin(),
                user.getAge(),
                user.getPassword(),
                user.getRole());
    }

    public UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getLogin(),
                user.getAge(),
                user.getRole());
    }

    public UserloginResponseDto toRegDto(User user) {
        return new UserloginResponseDto(
                user.getId(),
                user.getLogin(),
                user.getAge(),
                user.getRole());
    }

}
