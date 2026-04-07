package com.mealmanager.MealManagementSystem.controller;

import com.mealmanager.MealManagementSystem.dto.ApiResponse;
import com.mealmanager.MealManagementSystem.entity.ClosedMonthSummary;
import com.mealmanager.MealManagementSystem.entity.Session;
import com.mealmanager.MealManagementSystem.service.ReportService;
import com.mealmanager.MealManagementSystem.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private SessionService sessionService;

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSessionReport(@PathVariable Long sessionId) {
        Map<String, Object> report = reportService.getSessionReport(sessionId);
        return ResponseEntity.ok(ApiResponse.success(report, "Session report retrieved successfully"));
    }

    @GetMapping("/closed/{sessionId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getClosedSessionReport(@PathVariable Long sessionId) {
        Map<String, Object> report = reportService.getClosedSessionReport(sessionId);
        return ResponseEntity.ok(ApiResponse.success(report, "Closed session report retrieved successfully"));
    }

    @PostMapping("/close/{sessionId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> closeMonth(@PathVariable Long sessionId) {
        Map<String, Object> report = reportService.closeMonth(sessionId);
        return ResponseEntity.ok(ApiResponse.success(report, "Month closed successfully"));
    }

    @GetMapping("/closed-months")
    public ResponseEntity<ApiResponse<List<ClosedMonthSummary>>> getAllClosedMonths() {
        List<ClosedMonthSummary> closedMonths = reportService.getAllClosedMonths();
        return ResponseEntity.ok(ApiResponse.success(closedMonths, "Closed months retrieved successfully"));
    }

    @GetMapping("/active-session")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getActiveSessionReport() {
        Session activeSession = sessionService.getActiveSession()
                .orElseThrow(() -> new RuntimeException("No active session found"));
        Map<String, Object> report = reportService.getSessionReport(activeSession.getId());
        return ResponseEntity.ok(ApiResponse.success(report, "Active session report retrieved successfully"));
    }
}