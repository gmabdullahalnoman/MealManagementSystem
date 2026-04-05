package com.mealmanager.MealManagementSystem.controller;

import com.mealmanager.MealManagementSystem.entity.Session;
import com.mealmanager.MealManagementSystem.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
    @ResponseBody
    public Session getActiveSessionApi() {
        return sessionService.getActiveSession().orElse(null);
    }
    
    @GetMapping("/api/all")
    @ResponseBody
    public List<Session> getAllSessionsApi() {
        return sessionService.getAllSessions();
    }
    
    @GetMapping("/api/{id}")
    @ResponseBody
    public Session getSessionByIdApi(@PathVariable Long id) {
        return sessionService.getSessionById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }
    
    @PostMapping("/api/create")
    @ResponseBody
    public Session createSessionApi(@RequestParam String name, 
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        return sessionService.createSession(name, startDate);
    }
    
    @PostMapping("/api/{id}/close")
    @ResponseBody
    public Session closeSessionApi(@PathVariable Long id) {
        return sessionService.closeSession(id);
    }
}