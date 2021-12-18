package com.upfordown.ismuer.core.exception;

public class ErrorMessage {
    private String cause;
    private String message;

    public ErrorMessage(String cause, String message) {
        this.cause = cause;
        this.message = message;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
