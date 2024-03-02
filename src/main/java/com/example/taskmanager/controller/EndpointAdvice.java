package com.example.taskmanager.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.taskmanager.exception.CustomException;
import com.example.taskmanager.exception.ExceptionForm;

@ControllerAdvice
public class EndpointAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionForm> customExceptionHandler(CustomException exception, HttpServletRequest request) {
        ExceptionForm exceptionform = new ExceptionForm(
                exception.getTimestamp(),
                exception.getStatus(),
                exception.getClass().getCanonicalName(),
                exception.getMessage(),
                request.getServletPath()
        );
        return new ResponseEntity<>(exceptionform, exception.getStatus());
    }
}
