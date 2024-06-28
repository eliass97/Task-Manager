package com.example.taskmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.taskmanager.model.persistance.TaskStatus;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {

    Optional<TaskStatus> findByName(String name);
}
