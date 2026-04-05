package com.mealmanager.MealManagementSystem.service;

import com.mealmanager.MealManagementSystem.entity.Session;
import com.mealmanager.MealManagementSystem.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Transactional
    public Session createSession(String name, LocalDate startDate) {    // Create a new session
        // Deactivate any currently active session
        Optional<Session> activeSession = sessionRepository.findByIsActiveTrue();
        if (activeSession.isPresent()) {
            Session currentActive = activeSession.get();
            currentActive.setIsActive(false);
            sessionRepository.save(currentActive);
        }

        Session newSession = new Session(name, startDate);
        newSession.setIsActive(true);
        newSession.setIsClosed(false);
        return sessionRepository.save(newSession);
    }

    public Optional<Session> getActiveSession() {    // Get current active session
        return sessionRepository.findByIsActiveTrue();
    }

    public List<Session> getAllSessions() {    // Get all sessions
        return sessionRepository.findAll();
    }

    public Optional<Session> getSessionById(Long id) {    // Get session by ID
        return sessionRepository.findById(id);
    }

    public Session closeSession(Long id) {    // Close a session (no further edits allowed)
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        
        session.setIsClosed(true);
        session.setIsActive(false);
        session.setEndDate(LocalDate.now());
        
        return sessionRepository.save(session);
    }

    public boolean isSessionClosed(Long id) {    // Check if session is closed
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        return session.getIsClosed();
    }
}