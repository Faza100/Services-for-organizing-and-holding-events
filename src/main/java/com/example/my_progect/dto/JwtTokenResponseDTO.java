package com.example.my_progect.dto;

public class JwtTokenResponseDTO {

    private String JwtToken;

    public JwtTokenResponseDTO(
            String JwtToken) {
        this.JwtToken = JwtToken;
    }

    public String getJwtTokenResponseDTO() {
        return JwtToken;
    }

    public void setJwtTokenResponseDTO(
            String JwtToken) {
        this.JwtToken = JwtToken;
    }
}
