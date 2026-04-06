// Meal Management System - Main JavaScript

document.addEventListener('DOMContentLoaded', function() {
    console.log('MealManagementSystem loaded successfully');
    
    // Initialize Bootstrap tooltips and popovers
    initializeBootstrapComponents();
    
    // Auto-dismiss alerts after 5 seconds
    autoDismissAlerts();
});

/**
 * Initialize Bootstrap tooltips and popovers
 */
function initializeBootstrapComponents() {
    // Initialize all tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Initialize all popovers
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
}

/**
 * Auto-dismiss alerts after 5 seconds
 */
function autoDismissAlerts() {
    var alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            var bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });
}

/**
 * Format currency values
 */
function formatCurrency(value) {
    return new Intl.NumberFormat('en-BD', {
        style: 'currency',
        currency: 'BDT'
    }).format(value);
}

/**
 * Show loading spinner
 */
function showLoadingSpinner() {
    var spinner = document.createElement('div');
    spinner.className = 'spinner-overlay';
    spinner.innerHTML = '<div class="spinner-border text-primary" role="status"><span class="visually-hidden">Loading...</span></div>';
    document.body.appendChild(spinner);
}

/**
 * Hide loading spinner
 */
function hideLoadingSpinner() {
    var spinner = document.querySelector('.spinner-overlay');
    if (spinner) {
        spinner.remove();
    }
}