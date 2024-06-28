package com.example.taskmanager.model.dto;

import java.time.LocalDateTime;

import com.example.taskmanager.model.persistance.Comment;

public class CommentDTO {

    private Long id;
    private String description;
    private Integer writerId;
    private String writerName;
    private Long taskId;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWriterId() {
        return writerId;
    }

    public void setWriterId(Integer writerId) {
        this.writerId = writerId;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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

    public static CommentDTO fromObject(Comment comment, Integer writerId, String writerName, Long taskId) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setDescription(comment.getDescription());
        commentDTO.setWriterId(writerId);
        commentDTO.setWriterName(writerName);
        commentDTO.setTaskId(taskId);
        commentDTO.setCreationDate(comment.getCreationDate());
        commentDTO.setLastUpdateDate(comment.getLastUpdateDate());
        return commentDTO;
    }
}
