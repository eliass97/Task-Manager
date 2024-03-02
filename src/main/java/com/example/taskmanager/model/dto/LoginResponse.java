package com.example.taskmanager.model.dto;

import java.time.Instant;

public class LoginResponse {

    private String token;
    private String refreshToken;
    private Instant expiresAt;

    public LoginResponse(String token, String refreshToken, Instant expiresAt) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
}
