package com.mealmanager.MealManagementSystem.exception;

/**
 * Exception thrown when attempting to modify a closed session
 */
public class SessionClosedException extends RuntimeException {
    public SessionClosedException(String message) {
        super(message);
    }

    public SessionClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}
