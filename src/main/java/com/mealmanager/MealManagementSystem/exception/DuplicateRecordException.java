package com.mealmanager.MealManagementSystem.exception;

/**
 * Exception thrown when attempting to create a duplicate record
 */
public class DuplicateRecordException extends RuntimeException {
    public DuplicateRecordException(String message) {
        super(message);
    }

    public DuplicateRecordException(String message, Throwable cause) {
        super(message, cause);
    }
}
