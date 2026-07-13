package com.taskmanager.exception;

/**
 * Thrown when a requested resource (task, project, user) doesn't exist.
 * The GlobalExceptionHandler catches this and returns a 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
