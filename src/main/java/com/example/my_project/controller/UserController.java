package com.example.my_project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.my_project.dto.JwtTokenResponseDto;
import com.example.my_project.dto.user.UserloginResponseDto;
import com.example.my_project.dto.user.LoginRequestDto;
import com.example.my_project.dto.user.UserRequestDto;
import com.example.my_project.dto.user.UserResponseDto;
import com.example.my_project.mapper.UserMapper;
import com.example.my_project.model.User;
import com.example.my_project.service.AuthenticationService;
import com.example.my_project.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "JWT")
public class UserController {

        public final UserService userService;
        public final UserMapper userConverter;
        public final AuthenticationService authenticationService;
        private static final Logger log = LoggerFactory.getLogger(UserController.class);

        public UserController(
                        UserService userService,
                        UserMapper userConverter,
                        AuthenticationService authenticationService) {
                this.userService = userService;
                this.userConverter = userConverter;
                this.authenticationService = authenticationService;
        }

        @GetMapping("/{userId}")
        public ResponseEntity<UserResponseDto> getUserById(
                        @PathVariable Long userId) {
                User user = userService.getUserById(userId);
                return ResponseEntity.ok(userConverter.toDto(user));
        }

        @PostMapping
        public ResponseEntity<UserloginResponseDto> registerUser(
                        @RequestBody @Valid UserRequestDto registerUserDto) {
                log.info("User register event request: {}",
                                registerUserDto.getLogin());
                User regUser = userService.registerUser(
                                userConverter.toModel(registerUserDto));
                UserloginResponseDto response = userConverter.toRegDto(regUser);
                log.info("User register successfully: {}",
                                regUser.getLogin());
                return ResponseEntity.status(201)
                                .body(response);
        }

        @PostMapping("/aut")
        public ResponseEntity<JwtTokenResponseDto> authenticate(
                        @RequestBody @Valid LoginRequestDto loginRequest) {
                log.info("Request for aunteficate user: {}", loginRequest.getLogin());
                var Token = authenticationService.authenticateUser(loginRequest);
                return ResponseEntity.ok(new JwtTokenResponseDto(Token));
        }

}