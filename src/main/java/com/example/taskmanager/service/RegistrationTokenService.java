package com.example.taskmanager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.taskmanager.exception.CustomException;
import com.example.taskmanager.model.persistance.RegistrationToken;
import com.example.taskmanager.model.persistance.SystemUser;
import com.example.taskmanager.repository.RegistrationTokenRepository;

@Service
public class RegistrationTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationTokenService.class);

    @Value("${constants.registration-token-expiration-minutes}")
    private Long tokenExpirationMinutes;

    private final RegistrationTokenRepository registrationTokenRepository;

    public RegistrationTokenService(RegistrationTokenRepository registrationTokenRepository) {
        this.registrationTokenRepository = registrationTokenRepository;
    }

    protected RegistrationToken createToken(SystemUser systemUser) {
        LOGGER.info("Creating registration token for user id: {}", systemUser.getId());
        RegistrationToken registrationToken = new RegistrationToken();
        registrationToken.setToken(UUID.randomUUID().toString());
        registrationToken.setCreationDate(LocalDateTime.now());
        registrationToken.setExpirationDate(LocalDateTime.now().plusMinutes(tokenExpirationMinutes));
        registrationToken.setSystemUser(systemUser);
        return registrationTokenRepository.save(registrationToken);
    }

    public RegistrationToken getByToken(String token) throws CustomException {
        return registrationTokenRepository.findByToken(token)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Registration token does not exist"));
    }

    public void deleteById(Long id) {
        LOGGER.info("Deleting registration token with id: {}", id);
        registrationTokenRepository.deleteById(id);
    }

    public List<RegistrationToken> getExpiredRegistrationTokens() {
        LocalDateTime now = LocalDateTime.now();
        return registrationTokenRepository.findByExpirationDateBefore(now);
    }
}
