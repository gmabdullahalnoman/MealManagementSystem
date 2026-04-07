# Meal Management System - REST API

> A production-ready REST API for managing household/small business meal expenses and calculations

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Quick Start](#quick-start)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Development](#development)

---

## 🎯 Overview

The Meal Management System is a REST API designed to help groups (families, roommates, small teams, organizations) manage shared meal expenses efficiently:

- **Track Meals**: Record meal events with costs and participants
- **Manage Deposits**: Accept and manage financial contributions from members
- **Track Expenses**: Record all expenses related to meal management
- **Generate Reports**: Get detailed reports with settlement calculations
- **Session Management**: Organize accounting by sessions/periods
- **Fair Distribution**: Automatically calculate fair expense distribution

## ✨ Key Features

- **RESTful API**: Standard REST endpoints for all operations
- **Real-time Reports**: Get instant settlement calculations
- **Member Management**: Add, activate, and manage group members
- **Session-based Accounting**: Separate accounting periods
- **Comprehensive Validation**: Input validation with detailed error messages
- **API Documentation**: Auto-generated Swagger/OpenAPI documentation
- **PostgreSQL Database**: Production-grade database with migrations
- **Error Handling**: Standardized error responses

## 🛠 Technology Stack

- **Java 17** - Latest LTS version
- **Spring Boot 4.0.5** - Modern Spring framework
- **Spring Data JPA** - Object-relational mapping
- **PostgreSQL** - Primary database
- **Flyway** - Database migrations
- **SpringDoc OpenAPI 2.0.2** - API documentation (Swagger)
- **Jakarta Validation** - Request validation
- **Lombok** - Code generation

## 🚀 Quick Start

### Prerequisites

- Java 17 or higher
- PostgreSQL 12+ installed and running
- Maven 3.8+

### 1. Clone & Setup

```bash
git clone <repository-url>
cd MealManagementSystem
```

### 2. Configure PostgreSQL

Create a PostgreSQL database:

```sql
CREATE DATABASE mealmanagementdb;
```

### 3. Configure Application

Update `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/mealmanagementdb
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### 4. Build & Run

```bash
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## ⚙️ Configuration

### application.properties

Key configuration properties:

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/mealmanagementdb
spring.datasource.username=postgres
spring.datasource.password=postgres

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

# Logging
logging.level.com.mealmanager=DEBUG
logging.level.root=INFO
```

### Development Profile

Create `application-dev.properties` for local development (automatically used with `-Dspring.profiles.active=dev`)

---

## 📚 API Documentation

### Accessing API Documentation

Once the application is running:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **OpenAPI YAML**: http://localhost:8080/v3/api-docs.yaml

All endpoints are documented with descriptions, parameters, and response examples.

### Core API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| **Members** ||||
| GET | `/api/members` | List all members |
| GET | `/api/members/{id}` | Get member details |
| GET | `/api/members/active` | List active members |
| POST | `/api/members` | Create new member |
| PUT | `/api/members/{id}` | Update member |
| PATCH | `/api/members/{id}/activate` | Activate member |
| PATCH | `/api/members/{id}/deactivate` | Deactivate member |
| **Sessions** ||||
| GET | `/api/sessions` | List all sessions |
| GET | `/api/sessions/{id}` | Get session details |
| GET | `/api/sessions/active` | Get active session |
| POST | `/api/sessions` | Create new session |
| POST | `/api/sessions/{id}/close` | Close a session |
| **Deposits** ||||
| GET | `/api/deposits/session/{sessionId}` | Get session deposits |
| GET | `/api/deposits/session/{sessionId}/total` | Get total deposits |
| POST | `/api/deposits` | Record deposit |
| DELETE | `/api/deposits/{id}` | Delete deposit |
| **Expenses** ||||
| GET | `/api/expenses/session/{sessionId}` | Get session expenses |
| GET | `/api/expenses/session/{sessionId}/total` | Get total expenses |
| POST | `/api/expenses` | Record expense |
| PUT | `/api/expenses/{id}` | Update expense |
| DELETE | `/api/expenses/{id}` | Delete expense |
| **Meals** ||||
| GET | `/api/meals/session/{sessionId}` | Get session meals |
| POST | `/api/meals` | Record meal |
| PUT | `/api/meals/{id}` | Update meal |
| DELETE | `/api/meals/{id}` | Delete meal |
| **Reports** ||||
| GET | `/api/reports/session/{sessionId}` | Get session report |
| GET | `/api/reports/active-session` | Get active session report |
| GET | `/api/reports/closed-months` | List closed months |
| POST | `/api/reports/close/{sessionId}` | Close session |

### Example Requests

**Create a Member:**
```bash
curl -X POST http://localhost:8080/api/members \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","phone":"01711234567"}'
```

**Create a Session:**
```bash
curl -X POST http://localhost:8080/api/sessions \
  -H "Content-Type: application/json" \
  -d '{"name":"April 2026","startDate":"2026-04-01"}'
```

**Record a Deposit:**
```bash
curl -X POST http://localhost:8080/api/deposits \
  -H "Content-Type: application/json" \
  -d '{"sessionId":1,"memberId":1,"amount":5000,"depositDate":"2026-04-07"}'
```

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/mealmanager/MealManagementSystem/
│   │   ├── controller/          # REST Controllers
│   │   ├── service/             # Business logic
│   │   ├── repository/          # Data access layer
│   │   ├── entity/              # JPA entities
│   │   ├── dto/                 # Data transfer objects
│   │   ├── exception/           # Custom exceptions & handlers
│   │   ├── config/              # Spring configurations
│   │   └── util/                # Utility classes
│   └── resources/
│       ├── application.properties
│       └── db/migration/        # Flyway migrations
└── test/                        # Test classes
```

---

## 🔧 Development

### Running Tests

```bash
./mvnw test
```

### Adding Database Migrations

Create a new migration file in `src/main/resources/db/migration/`:

```sql
-- V2__Add_new_feature.sql
ALTER TABLE members ADD COLUMN new_field VARCHAR(100);
```

Flyway automatically runs migrations on startup.

### Error Response Format

All errors return a standardized format:

```json
{
  "status": 400,
  "message": "Validation failed",
  "error": "Validation Failed",
  "timestamp": "2026-04-07T10:30:00",
  "path": "/api/members"
}
```

---

## 📝 Notes

- All endpoints are prefixed with `/api/`
- API responses use standardized `ApiResponse<T>` wrapper
- Validation errors include field-level details
- Swagger UI is automatically available at `/swagger-ui.html`
- Database schema is managed by Flyway
- Request validation uses Jakarta Bean Validation annotations

## 📄 License

This project is licensed under the Apache 2.0 License - see the LICENSE file for details.

---

**Built with ❤️ for efficient meal management**
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