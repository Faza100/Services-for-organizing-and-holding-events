package com.example.my_project.dto.user;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.Size;

public class LoginRequestDto {

    @NonNull
    @Size(min = 2, max = 15, message = "Name must be between 2 and 15 characters")
    private String login;

    @NonNull
    @Size(min = 6, max = 25, message = "Password must be between 6 and 25 characters")
    private String password;

    public LoginRequestDto(
            String login,
            String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(
            String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(
            String password) {
        this.password = password;
    }
}
