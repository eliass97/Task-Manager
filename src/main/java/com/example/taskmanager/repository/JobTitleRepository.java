package com.example.taskmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.taskmanager.model.persistance.JobTitle;

@Repository
public interface JobTitleRepository extends JpaRepository<JobTitle, Long> {

    Optional<JobTitle> findByName(String name);
}
