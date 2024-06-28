package com.example.taskmanager.model.dto;

import java.time.LocalDateTime;

import com.example.taskmanager.model.persistance.Task;

public class TaskDTO {

    private Long id;
    private String name;
    private String description;
    private Integer assigneeId;
    private String assigneeName;
    private TypeDTO taskStatus;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Integer assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public TypeDTO getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TypeDTO taskStatus) {
        this.taskStatus = taskStatus;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public static TaskDTO fromObject(Task task, Integer assigneeId, String assigneeName) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setName(task.getName());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setAssigneeId(assigneeId);
        taskDTO.setAssigneeName(assigneeName);
        taskDTO.setTaskStatus(TypeDTO.fromObject(task.getTaskStatus()));
        taskDTO.setCreationDate(task.getCreationDate());
        taskDTO.setLastUpdateDate(task.getLastUpdateDate());
        return taskDTO;
    }
}
