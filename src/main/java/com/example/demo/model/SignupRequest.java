package com.example.demo.model;

public class SignupRequest {
    private String username;
    private String password;
    private String role;

    public SignupRequest(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }


    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "SignupRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
