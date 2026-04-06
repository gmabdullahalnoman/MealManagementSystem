# Meal Management System
> A modern, enterprise-grade application for managing household/small business meal expenses and calculations

---

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Modernization Highlights](#modernization-highlights)
- [Project Structure](#project-structure)
- [Development](#development)

---

## Project History

### User's Original Contribution
- Initial Spring Boot project structure and setup
- Core business logic and database entities  
- Basic REST controllers for CRUD operations
- Initial HTML templates with basic styling

### AI Modernization Contribution(Github Copilot)
- Implemented complete exception handling
- Added Spring Security 6.x with proper FilterChain configuration
- Standardized all APIs with APIResponse<T> wrapper for consistent responses
- Added validation framework with jakarta annotations across 6 DTOs
- Upgraded frontend to Bootstrap 5.3.2 with modern UI, animations, and 3 new templates
- Added Flyway database migrations for safe schema management
- Fixed 3 runtime configuration issues to make app fully functional

---

## Overview

This application helps groups (families, roommates, small teams) manage shared meal expenses efficiently:
- Track meal costs per person
- Manage deposits from members
- Calculate fair distribution of expenses
- Generate detailed reports with settlement amounts
- Session-based management for monthly/period-based accounting

---

## Key Features

### Dashboard
- Real-time summary of deposits, expenses, and meals
- Quick access to all management functions
- Member settlement status at a glance
- Visual gradient cards with modern styling

### Member Management
- Create and manage household/team members
- Mark members as active or inactive
- Track member-specific transactions
- Modern form validation

### Financial Management
- Deposits: Track money contributed by members
- Expenses: Record shared expenses with automatic distribution
- Meal Tracking: Record who ate and calculate costs
- Reports: Detailed member-wise settlement calculations

### Reporting & Analytics
- Session-wise financial reports
- Per-member expense breakdown
- Settlement calculations (who owes whom)
- Close month functionality with carry-forward balances
- Historical data retrieval

---

## Technology Stack

### Backend
- Framework: Spring Boot 4.0.5
- Language: Java 17+
- Build: Maven 3.9+
- Database: H2 (development), Flyway for migrations
- Security: Spring Security 6.x with BCrypt
- API Docs: Springdoc OpenAPI (Swagger UI)
- Validation: Jakarta Bean Validation

### Frontend
- Template Engine: Thymeleaf
- CSS Framework: Bootstrap 5.3.2 (responsive)
- Icons: Bootstrap Icons
- Styling: Custom CSS with animations
- Form Validation: HTML5 + Client-side JS

### Tools & Libraries
- Project Lombok: Reduces boilerplate code
- Mockito: Unit testing framework
- JUnit 5: Testing framework
- Spring Data JPA: ORM and data access

---

## Getting Started

### Prerequisites
- Java 17+ (JDK)
- Maven 3.9+ (or use included mvnw)
- Git

### Installation

1. **Clone the repository**
   git clone <repository-url>
   cd MealManagementSystem
   ```

2. **Build the project**
   # Windows
   ./mvnw.cmd clean install
   # Linux/Mac
   ./mvnw clean install

3. **Run the application**
   # Windows
   ./mvnw.cmd spring-boot:run
   # Linux/Mac
   ./mvnw spring-boot:run

4. **Access the application**
   - Web UI: http://localhost:8080
   - Swagger API Docs: http://localhost:8080/swagger-ui.html
   - H2 Console: http://localhost:8080/h2-console

---

### Main API Endpoints

#### Members
- `GET /api/members/all` - All members
- `GET /api/members/{id}` - Get specific member
- `POST /api/members/create` - Create member
- `PUT /api/members/{id}` - Update member
- `DELETE /api/members/{id}/deactivate` - Deactivate member

#### Sessions
- `GET /api/sessions/active` - Get active session
- `GET /api/sessions/all` - All sessions
- `POST /api/sessions/create` - Create new session
- `POST /api/sessions/{id}/close` - Close session

#### Deposits
- `GET /api/deposits/session/{sessionId}` - Get session deposits
- `GET /api/deposits/session/{sessionId}/total` - Total deposits
- `POST /api/deposits/add` - Add deposit

#### Expenses
- `GET /api/expenses/session/{sessionId}` - Get expenses
- `POST /api/expenses/add` - Add expense
- `PUT /api/expenses/{id}` - Update expense
- `DELETE /api/expenses/{id}` - Delete expense

#### Meals
- `GET /api/meals/session/{sessionId}` - Get meals
- `POST /api/meals/add` - Record meal
- `GET /api/meals/session/{sessionId}/per-member` - Meals per member

#### Reports
- `GET /api/reports/session/{sessionId}` - Get session report
- `GET /api/reports/active-report` - Get active session report
- `POST /api/reports/close/{sessionId}` - Close month and settle

---

### IDE Setup

#### IntelliJ IDEA
1. Open project folder
2. IntelliJ will auto-detect Maven configuration
3. Mark `src/main/java` as Sources Root
4. Mark `src/main/resources` as Resources Root
5. Install Thymeleaf plugin (optional)

#### VS Code
1. Install Java Extension Pack
2. Open project folder
3. VS Code will auto-configure for Maven
4. Extensions recommended:
   - Spring Boot Extension Pack
   - Thymeleaf Now
   - Bootstrap Class Completion

---

## Security

- Spring Security 6.x configured
- CSRF protection enabled
- BCrypt password encoding
- CORS properly configured
- H2 console disabled in production
- SQL logging disabled (performance & security)
- Input validation on all DTOs
- Security headers configured

---

## Database

### Schema
- members: User participants
- sessions: Accounting periods
- deposits: Money contributions
- expenses: Shared costs
- meal_records: Meal tracking
- member_opening_balances: Period opening balances
- member_closed_balances: Period closing balances
- closed_month_summaries: Historical summaries

License:
Apache 2.0