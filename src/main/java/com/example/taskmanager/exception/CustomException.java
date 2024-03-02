package com.example.taskmanager.exception;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;

public class CustomException extends Exception {

    private HttpStatus status;
    private final Timestamp timestamp;

    public CustomException() {
        super();
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    public CustomException(String message) {
        super(message);
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    public CustomException(HttpStatus status) {
        super();
        timestamp = new Timestamp(System.currentTimeMillis());
        this.status = status;
    }

    public CustomException(HttpStatus status, String message) {
        super(message);
        timestamp = new Timestamp(System.currentTimeMillis());
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}