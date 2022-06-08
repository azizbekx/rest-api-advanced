package com.epam.esm.exception;

public class ResourceNotFoundException extends RuntimeException {
    private long objectId;
    private String errorMessage;

    public ResourceNotFoundException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ResourceNotFoundException(long objectId, String errorMessage) {
        this.objectId = objectId;
        this.errorMessage = errorMessage;
    }

    public ResourceNotFoundException(long objectId) {
        this.objectId = objectId;
    }

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
