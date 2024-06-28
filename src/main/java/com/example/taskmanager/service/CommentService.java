package com.example.taskmanager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.taskmanager.exception.CustomException;
import com.example.taskmanager.model.dto.CommentDTO;
import com.example.taskmanager.model.persistance.Comment;
import com.example.taskmanager.model.persistance.SystemUser;
import com.example.taskmanager.model.persistance.Task;
import com.example.taskmanager.model.persistance.UserProfile;
import com.example.taskmanager.repository.CommentRepository;

@Service
public class CommentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final SystemUserService systemUserService;

    public CommentService(
            CommentRepository commentRepository,
            TaskService taskService,
            SystemUserService systemUserService
    ) {
        this.commentRepository = commentRepository;
        this.taskService = taskService;
        this.systemUserService = systemUserService;

    }

    public List<CommentDTO> getCommentsByTask(Long taskId) {
        List<Comment> comments = commentRepository.findByTaskId(taskId);
        return comments.stream()
                .map(this::convertCommentToDTO)
                .collect(Collectors.toList());
    }

    public Comment getCommentById(Long id) throws CustomException {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Comment not found"));
    }

    @Transactional(rollbackOn = CustomException.class)
    public CommentDTO createComment(CommentDTO commentDTO) throws CustomException {
        doBasicValidationChecks(commentDTO);

        SystemUser writer = systemUserService.getUserById(commentDTO.getWriterId());
        Task task = taskService.getTaskById(commentDTO.getTaskId());

        LOGGER.info("Creating comment from user id: {} on task id: {}", commentDTO.getWriterId(), commentDTO.getTaskId());
        Comment newComment = new Comment();
        populateComment(newComment, commentDTO, writer, task);

        Comment createdComment = commentRepository.save(newComment);
        return convertCommentToDTO(createdComment);
    }

    @Transactional(rollbackOn = CustomException.class)
    public CommentDTO updateComment(CommentDTO commentDTO) throws CustomException {
        Comment comment = getCommentById(commentDTO.getId());
        LocalDateTime lastUpdateDate = commentDTO.getLastUpdateDate();
        if (lastUpdateDate == null || comment.getLastUpdateDate().isAfter(commentDTO.getLastUpdateDate())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Comment data are out-dated");
        }

        doBasicValidationChecks(commentDTO);

        SystemUser writer = systemUserService.getUserById(commentDTO.getWriterId());
        Task task = taskService.getTaskById(commentDTO.getTaskId());
        LOGGER.info("Updating comment with id: {}", commentDTO.getId());
        populateComment(comment, commentDTO, writer, task);

        Comment updatedComment = commentRepository.save(comment);
        return convertCommentToDTO(updatedComment);
    }

    public void deleteComment(Long id) throws CustomException {
        Comment task = getCommentById(id);
        commentRepository.deleteById(task.getId());
    }

    private CommentDTO convertCommentToDTO(Comment comment) {
        SystemUser writer = comment.getWriter();
        UserProfile userProfile = writer.getProfile();
        String fullName = String.format("%s %s", userProfile.getFirstName(), userProfile.getLastName());
        return CommentDTO.fromObject(comment, writer.getId(), fullName, comment.getTask().getId());
    }

    private void doBasicValidationChecks(CommentDTO commentDTO) throws CustomException {
        String description = commentDTO.getDescription();
        if (!StringUtils.hasText(description)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Comment description is empty");
        }

        if (commentDTO.getWriterId() == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Comment writer is empty");
        }
    }

    private void populateComment(Comment comment, CommentDTO commentDTO, SystemUser writer, Task task) {
        comment.setDescription(commentDTO.getDescription());
        comment.setWriter(writer);
        comment.setTask(task);
    }
}
