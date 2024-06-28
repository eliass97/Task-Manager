package com.example.taskmanager.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.taskmanager.exception.CustomException;
import com.example.taskmanager.exception.ExceptionForm;

@ControllerAdvice
public class EndpointAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndpointAdvice.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionForm> customExceptionHandler(CustomException exception, HttpServletRequest request) {
        LOGGER.error(exception.getMessage(), exception);
        ExceptionForm exceptionform = new ExceptionForm(
                exception.getMessage(),
                exception.getStatus(),
                exception.getTimestamp()
        );
        return new ResponseEntity<>(exceptionform, exception.getStatus());
    }
}
