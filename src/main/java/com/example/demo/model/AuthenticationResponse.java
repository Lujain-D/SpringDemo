package com.example.demo.model;

public class AuthenticationResponse {
    private String jwt;
    private String username;

    public String getJwt() {
        return jwt;
    }

    public String getUsername() {
        return username;
    }

    public AuthenticationResponse(String jwt, String username) {
        this.jwt = jwt;
        this.username = username;
    }
}
