package com.example.taskmanager.exception;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExceptionForm {

    private Timestamp timestamp;

    @JsonIgnore
    private HttpStatus status;
    private String exception;
    private String message;
    private String path;

    public ExceptionForm(Timestamp timestamp, HttpStatus status, String exception, String message, String path) {
        setTimestamp(timestamp);
        setStatus(status);
        setException(exception);
        setMessage(message);
        setPath(path);
    }

    @JsonProperty("status")
    public int getStatusCode() {
        return status.value();
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getException() {
        return exception;
    }

    public String getPath() {
        return path;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
