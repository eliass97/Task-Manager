package com.example.taskmanager.service;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.example.taskmanager.exception.CustomException;
import com.example.taskmanager.model.dto.LoginRequest;
import com.example.taskmanager.model.dto.LoginResponse;
import com.example.taskmanager.model.persistance.RefreshToken;
import com.example.taskmanager.model.persistance.SystemUser;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtProviderService jwtProviderService;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            JwtProviderService jwtProviderService,
            RefreshTokenService refreshTokenService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtProviderService = jwtProviderService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        SystemUser systemUser = (SystemUser) authentication.getPrincipal();
        Jwt jwt = jwtProviderService.generateToken(systemUser);
        String refreshToken = refreshTokenService.generateRefreshToken(authentication);
        return new LoginResponse(jwt.getTokenValue(), refreshToken, jwt.getExpiresAt());
    }

    @Transactional
    public LoginResponse refreshToken(LoginRequest loginRequest) throws CustomException {
        if (loginRequest.getRefreshToken() == null || loginRequest.getRefreshToken().isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Refresh token cannot be empty");
        }
        RefreshToken refreshToken = refreshTokenService.updateRefreshToken(loginRequest.getRefreshToken());
        Jwt jwt = jwtProviderService.generateToken(refreshToken.getSystemUser());
        return new LoginResponse(jwt.getTokenValue(), refreshToken.getToken(), jwt.getExpiresAt());
    }

    @Transactional
    public void logout(LoginRequest loginRequest) throws CustomException {
        if (loginRequest.getRefreshToken() == null || loginRequest.getRefreshToken().isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Refresh token cannot be empty");
        }
        refreshTokenService.deleteByToken(loginRequest.getRefreshToken());
    }
}
