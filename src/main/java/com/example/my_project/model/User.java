package com.example.my_project.model;

import com.example.my_project.enums.UserRole;

public class User {

    private Long id;

    private String login;

    private Integer age;

    private String password;

    private UserRole role;

    public User(String login,
            Integer age,
            String password) {
        this.login = login;
        this.age = age;
        this.password = password;
        this.role = UserRole.USER;
    }

    public User(
            Long id,
            String login,
            Integer age,
            String password,
            UserRole role) {
        this.id = id;
        this.login = login;
        this.age = age;
        this.password = password;
        this.role = role;
    }

    public User(
            String login,
            Integer age,
            String password,
            UserRole role) {
        this.login = login;
        this.age = age;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
