package com.example.taskmanager.exception;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExceptionForm {

    private String message;
    @JsonIgnore
    private HttpStatus status;
    private Timestamp timestamp;

    public ExceptionForm(String message, HttpStatus status, Timestamp timestamp) {
        setMessage(message);
        setStatus(status);
        setTimestamp(timestamp);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("status")
    public int getStatusCode() {
        return status.value();
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

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
