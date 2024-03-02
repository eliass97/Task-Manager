package com.example.taskmanager.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.example.taskmanager.model.persistance.SystemAuthority;
import com.example.taskmanager.model.persistance.SystemUser;

@Service
public class JwtProviderService {

    private final JwtEncoder jwtEncoder;

    @Value("${constants.login-token-expiration-period}")
    private Long tokenExpirationPeriod;

    public JwtProviderService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public Jwt generateToken(SystemUser systemUser) {
        List<String> scopes = systemUser.getAuthorities().stream()
                .map(SystemAuthority::getAuthority)
                .collect(Collectors.toList());
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .subject(systemUser.getUsername())
                .claim("scp", scopes)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(tokenExpirationPeriod))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims));
    }
}
