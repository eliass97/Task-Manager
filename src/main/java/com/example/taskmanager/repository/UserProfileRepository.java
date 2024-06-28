package com.example.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.taskmanager.model.persistance.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
