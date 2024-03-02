package com.example.taskmanager.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.taskmanager.exception.CustomException;
import com.example.taskmanager.model.dto.TypeDTO;
import com.example.taskmanager.model.persistance.JobTitle;
import com.example.taskmanager.repository.JobTitleRepository;

@Service
public class JobTitleService {

    private final JobTitleRepository jobTitleRepository;

    public JobTitleService(JobTitleRepository jobTitleRepository) {
        this.jobTitleRepository = jobTitleRepository;
    }

    public List<TypeDTO> getAllJobTitles() {
        return jobTitleRepository.findAll().stream()
                .map(TypeDTO::fromObject)
                .collect(Collectors.toList());
    }

    public JobTitle getJobTitleByName(String name) throws CustomException {
        Optional<JobTitle> jobTitle = jobTitleRepository.findByName(name);
        return jobTitle.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Job Title not found"));
    }
}
