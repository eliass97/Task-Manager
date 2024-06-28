package com.example.taskmanager.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.taskmanager.model.persistance.RegistrationToken;

@Repository
public interface RegistrationTokenRepository extends JpaRepository<RegistrationToken, Long> {

    Optional<RegistrationToken> findByToken(String token);

    List<RegistrationToken> findByExpirationDateBefore(LocalDateTime localDateTime);
}
