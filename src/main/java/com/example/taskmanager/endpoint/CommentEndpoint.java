package com.example.taskmanager.endpoint;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.exception.CustomException;
import com.example.taskmanager.model.dto.CommentDTO;
import com.example.taskmanager.service.CommentService;

@RestController
@RequestMapping("api/comment")
public class CommentEndpoint {

    private final CommentService commentService;

    public CommentEndpoint(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{task_id}")
    public List<CommentDTO> getCommentsByTask(@PathVariable("task_id") Long taskId) {
        return commentService.getCommentsByTask(taskId);
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) throws CustomException {
        CommentDTO createdComment = commentService.createComment(commentDTO);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<CommentDTO> updateComment(@RequestBody CommentDTO commentDTO) throws CustomException {
        CommentDTO updatedComment = commentService.updateComment(commentDTO);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteComment(@PathVariable("id") Long id) throws CustomException {
        commentService.deleteComment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
