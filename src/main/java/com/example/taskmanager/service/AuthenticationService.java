package com.example.taskmanager.service;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

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
        LOGGER.info("Received login request for user: {}", loginRequest.getUsername());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        SystemUser systemUser = (SystemUser) authentication.getPrincipal();
        LOGGER.info("Generating login token for user: {}", loginRequest.getUsername());
        Jwt jwt = jwtProviderService.generateToken(systemUser);
        LOGGER.info("Generating refresh token for user: {}", loginRequest.getUsername());
        String refreshToken = refreshTokenService.generateRefreshToken(authentication);
        return new LoginResponse(systemUser.getId(), jwt.getTokenValue(), refreshToken, jwt.getExpiresAt());
    }

    @Transactional(rollbackOn = CustomException.class)
    public LoginResponse refreshToken(LoginRequest loginRequest) throws CustomException {
        if (loginRequest.getRefreshToken() == null || loginRequest.getRefreshToken().isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Refresh token cannot be empty");
        }
        LOGGER.info("Updating refresh token for user: {}", loginRequest.getUsername());
        RefreshToken refreshToken = refreshTokenService.updateRefreshToken(loginRequest.getRefreshToken());
        Jwt jwt = jwtProviderService.generateToken(refreshToken.getSystemUser());
        return new LoginResponse(jwt.getTokenValue(), refreshToken.getToken(), jwt.getExpiresAt());
    }

    @Transactional(rollbackOn = CustomException.class)
    public void logout(LoginRequest loginRequest) throws CustomException {
        if (loginRequest.getRefreshToken() == null || loginRequest.getRefreshToken().isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Refresh token cannot be empty");
        }
        refreshTokenService.deleteByToken(loginRequest.getRefreshToken());
    }
}
