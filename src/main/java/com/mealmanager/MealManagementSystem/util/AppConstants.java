package com.mealmanager.MealManagementSystem.util;

/**
 * Application constants
 */
public class AppConstants {

    // Meal Types
    public static final String MEAL_TYPE_LUNCH = "LUNCH";
    public static final String MEAL_TYPE_DINNER = "DINNER";
    public static final String MEAL_TYPE_BOTH = "BOTH";
    public static final String MEAL_TYPE_GUEST = "GUEST";

    // Deposit Types
    public static final String DEPOSIT_TYPE_REGULAR = "REGULAR";
    public static final String DEPOSIT_TYPE_EXTRA = "EXTRA";

    // Error Messages
    public static final String SESSION_NOT_FOUND = "Session not found";
    public static final String MEMBER_NOT_FOUND = "Member not found";
    public static final String DEPOSIT_NOT_FOUND = "Deposit not found";
    public static final String EXPENSE_NOT_FOUND = "Expense not found";
    public static final String MEAL_NOT_FOUND = "Meal not found";

    // Validation Messages
    public static final String SESSION_CLOSED = "Session is closed. No modifications allowed.";
    public static final String MEMBER_INACTIVE = "Member is inactive.";
    public static final String DUPLICATE_MEAL = "Duplicate meal entry for this member on this date.";

    // Pagination
    public static final int PAGE_SIZE = 20;
    public static final int DEFAULT_PAGE = 0;

    // Date Format
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // Response Messages
    public static final String OPERATION_SUCCESS = "Operation completed successfully";
    public static final String OPERATION_FAILED = "Operation failed";

    private AppConstants() {
        // Private constructor to prevent instantiation
    }
}
