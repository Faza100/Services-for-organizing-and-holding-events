package com.example.my_project.exeption;

public class NoRolesException extends RuntimeException {
    public NoRolesException(String message) {
        super(message);
    }
}
