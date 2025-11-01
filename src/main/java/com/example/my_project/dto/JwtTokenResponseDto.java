package com.example.my_project.dto;

public class JwtTokenResponseDto {

    private String JwtToken;

    public JwtTokenResponseDto(
            String JwtToken) {
        this.JwtToken = JwtToken;
    }

    public String getJwtTokenResponseDto() {
        return JwtToken;
    }

    public void setJwtTokenResponseDto(
            String JwtToken) {
        this.JwtToken = JwtToken;
    }
}
