package com.mealmanager.MealManagementSystem.controller;

import com.mealmanager.MealManagementSystem.dto.ApiResponse;
import com.mealmanager.MealManagementSystem.dto.SessionDTO;
import com.mealmanager.MealManagementSystem.entity.Session;
import com.mealmanager.MealManagementSystem.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Session>>> getAllSessions() {
        List<Session> sessions = sessionService.getAllSessions();
        return ResponseEntity.ok(ApiResponse.success(sessions, "Sessions retrieved successfully"));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<Session>> getActiveSession() {
        Session session = sessionService.getActiveSession().orElse(null);
        return ResponseEntity.ok(ApiResponse.success(session, "Active session retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Session>> getSessionById(@PathVariable Long id) {
        Session session = sessionService.getSessionById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        return ResponseEntity.ok(ApiResponse.success(session, "Session retrieved successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Session>> createSession(@Valid @RequestBody SessionDTO sessionDTO) {
        Session session = sessionService.createSession(sessionDTO.getName(), sessionDTO.getStartDate());
        return ResponseEntity.status(201).body(ApiResponse.success(session, "Session created successfully"));
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<ApiResponse<Void>> closeSession(@PathVariable Long id) {
        sessionService.closeSession(id);
        return ResponseEntity.ok(ApiResponse.success("Session closed successfully"));
    }
}