package com.example.taskmanager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.taskmanager.exception.CustomException;
import com.example.taskmanager.model.persistance.RefreshToken;
import com.example.taskmanager.model.persistance.SystemUser;
import com.example.taskmanager.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String generateRefreshToken(Authentication authentication) {
        SystemUser systemUser = (SystemUser) authentication.getPrincipal();
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setSystemUser(systemUser);
        refreshToken.setCreationDate(now);
        refreshToken.setLastUpdateDate(now);
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    public RefreshToken updateRefreshToken(String token) throws CustomException {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Refresh token not found"));
        refreshToken.setLastUpdateDate(LocalDateTime.now());
        return refreshTokenRepository.save(refreshToken);
    }

    public List<RefreshToken> getExpiredRegistrationTokens() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        return refreshTokenRepository.findByLastUpdateDateBefore(yesterday);
    }

    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    public void deleteById(Long id) {
        refreshTokenRepository.deleteById(id);
    }
}
