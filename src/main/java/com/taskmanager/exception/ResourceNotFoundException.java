package com.taskmanager.exception;

/**
 * Marks a missing domain resource such as a task, project, or user.
 * The global exception handler converts it into a 404 response.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Creates the exception with a readable message.
     * Input: a description of the missing resource.
     * Output: a runtime exception instance ready to be thrown.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
