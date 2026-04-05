package com.mealmanager.MealManagementSystem.controller;

import com.mealmanager.MealManagementSystem.entity.ClosedMonthSummary;
import com.mealmanager.MealManagementSystem.entity.Session;
import com.mealmanager.MealManagementSystem.service.ReportService;
import com.mealmanager.MealManagementSystem.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private SessionService sessionService;

    // ========== Thymeleaf Views ==========
    
    @GetMapping
    public String showReportPage(Model model) {
        List<ClosedMonthSummary> closedMonths = reportService.getAllClosedMonths();
        Session activeSession = sessionService.getActiveSession().orElse(null);
        
        model.addAttribute("closedMonths", closedMonths);
        model.addAttribute("activeSession", activeSession);
        model.addAttribute("hasActiveSession", activeSession != null);
        
        return "reports";
    }
    
    @GetMapping("/session/{sessionId}")
    public String viewSessionReport(@PathVariable Long sessionId, Model model) {
        Map<String, Object> report = reportService.getSessionReport(sessionId);
        
        model.addAttribute("report", report);
        model.addAttribute("isClosed", ((Session) report.get("session")).getIsClosed());
        
        return "report-view";
    }
    
    @GetMapping("/closed/{sessionId}")
    public String viewClosedSessionReport(@PathVariable Long sessionId, Model model) {
        Map<String, Object> report = reportService.getClosedSessionReport(sessionId);
        
        model.addAttribute("report", report);
        
        return "report-view";
    }
    
    @PostMapping("/close-month")
    public String closeCurrentMonth(Model model) {
        Session activeSession = sessionService.getActiveSession()
                .orElseThrow(() -> new RuntimeException("No active session found"));
        
        Map<String, Object> closedReport = reportService.closeMonth(activeSession.getId());
        
        model.addAttribute("report", closedReport);
        model.addAttribute("message", "Month closed successfully! New month created with carry forward balances.");
        
        return "report-view";
    }
    
    @GetMapping("/month-close")
    public String showMonthClosePage(Model model) {
        Session activeSession = sessionService.getActiveSession().orElse(null);
        
        if (activeSession == null) {
            model.addAttribute("error", "No active session found. Please create a session first.");
            return "month-close";
        }
        
        Map<String, Object> currentReport = reportService.getSessionReport(activeSession.getId());
        
        model.addAttribute("report", currentReport);
        model.addAttribute("activeSession", activeSession);
        
        return "month-close";
    }
    
    // ========== REST API Endpoints ==========
    
    @GetMapping("/api/session/{sessionId}")
    @ResponseBody
    public Map<String, Object> getSessionReportApi(@PathVariable Long sessionId) {
        return reportService.getSessionReport(sessionId);
    }
    
    @GetMapping("/api/closed/{sessionId}")
    @ResponseBody
    public Map<String, Object> getClosedSessionReportApi(@PathVariable Long sessionId) {
        return reportService.getClosedSessionReport(sessionId);
    }
    
    @PostMapping("/api/close/{sessionId}")
    @ResponseBody
    public Map<String, Object> closeMonthApi(@PathVariable Long sessionId) {
        return reportService.closeMonth(sessionId);
    }
    
    @GetMapping("/api/closed-months")
    @ResponseBody
    public List<ClosedMonthSummary> getAllClosedMonthsApi() {
        return reportService.getAllClosedMonths();
    }
    
    @GetMapping("/api/active-report")
    @ResponseBody
    public Map<String, Object> getActiveSessionReportApi() {
        Session activeSession = sessionService.getActiveSession()
                .orElseThrow(() -> new RuntimeException("No active session found"));
        return reportService.getSessionReport(activeSession.getId());
    }
}