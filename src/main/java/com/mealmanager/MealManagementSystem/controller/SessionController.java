package com.mealmanager.MealManagementSystem.controller;

import com.mealmanager.MealManagementSystem.dto.ApiResponse;
import com.mealmanager.MealManagementSystem.dto.SessionDTO;
import com.mealmanager.MealManagementSystem.entity.Session;
import com.mealmanager.MealManagementSystem.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    // ========== Thymeleaf Views ==========
    
    @GetMapping
    public String listSessions(Model model) {
        List<Session> sessions = sessionService.getAllSessions();
        model.addAttribute("sessions", sessions);
        model.addAttribute("activeSession", sessionService.getActiveSession().orElse(null));
        return "sessions";
    }
    
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("session", new Session());
        return "session-form";
    }
    
    @PostMapping("/create")
    public String createSession(@RequestParam String name, 
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        sessionService.createSession(name, startDate);
        return "redirect:/sessions";
    }
    
    @PostMapping("/{id}/close")
    public String closeSession(@PathVariable Long id) {
        sessionService.closeSession(id);
        return "redirect:/sessions";
    }
    
    // ========== REST API Endpoints ==========
    
    @GetMapping("/api/active")
    public ResponseEntity<ApiResponse<Session>> getActiveSessionApi() {
        Session session = sessionService.getActiveSession().orElse(null);
        return ResponseEntity.ok(ApiResponse.success(session, "Active session retrieved successfully"));
    }
    
    @GetMapping("/api/all")
    public ResponseEntity<ApiResponse<List<Session>>> getAllSessionsApi() {
        List<Session> sessions = sessionService.getAllSessions();
        return ResponseEntity.ok(ApiResponse.success(sessions, "Sessions retrieved successfully"));
    }
    
    @GetMapping("/api/{id}")
    public ResponseEntity<ApiResponse<Session>> getSessionByIdApi(@PathVariable Long id) {
        Session session = sessionService.getSessionById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        return ResponseEntity.ok(ApiResponse.success(session, "Session retrieved successfully"));
    }
    
    @PostMapping("/api/create")
    public ResponseEntity<ApiResponse<Session>> createSessionApi(@Valid @RequestBody SessionDTO sessionDTO) {
        Session session = sessionService.createSession(sessionDTO.getName(), sessionDTO.getStartDate());
        return ResponseEntity.ok(ApiResponse.success(session, "Session created successfully"));
    }
    
    @PostMapping("/api/{id}/close")
    public ResponseEntity<ApiResponse<Void>> closeSessionApi(@PathVariable Long id) {
        sessionService.closeSession(id);
        return ResponseEntity.ok(ApiResponse.success("Session closed successfully"));
    }
}