package com.example.taskmanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.taskmanager.model.persistance.SystemUser;

@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser, Integer> {

    Optional<SystemUser> findByUsername(String username);

    Optional<SystemUser> findByEmail(String email);

    boolean existsByUsername(String username);

    @Query("select su.id, su.email, p.firstName, p.lastName from SystemUser su left join su.profile p")
    List<Object[]> getUsersMinimalInfo();
}
