package com.infosys.exception;

import java.time.LocalDateTime;

public class ErrorMessage {

    private int errorCode;
    private String message;
    private LocalDateTime timestamp;

    public ErrorMessage() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorMessage(int errorCode, String message) {
        this();
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() { return errorCode; }
    public void setErrorCode(int errorCode) { this.errorCode = errorCode; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
