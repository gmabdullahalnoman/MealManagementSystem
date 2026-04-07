# Meal Management System - REST API

> A production-ready REST API for managing household/small business meal expenses and calculations

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Quick Start](#quick-start)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Development](#development)

---

## Overview

The Meal Management System is a REST API designed to help groups (families, roommates, small teams, organizations) manage shared meal expenses efficiently:

- **Track Meals**: Record meal events with costs and participants
- **Manage Deposits**: Accept and manage financial contributions from members
- **Track Expenses**: Record all expenses related to meal management
- **Generate Reports**: Get detailed reports with settlement calculations
- **Session Management**: Organize accounting by sessions/periods
- **Fair Distribution**: Automatically calculate fair expense distribution

## Key Features

- **RESTful API**: Standard REST endpoints for all operations
- **Real-time Reports**: Get instant settlement calculations
- **Member Management**: Add, activate, and manage group members
- **Session-based Accounting**: Separate accounting periods
- **Comprehensive Validation**: Input validation with detailed error messages
- **API Documentation**: Auto-generated Swagger/OpenAPI documentation
- **PostgreSQL Database**: Production-grade database with migrations
- **Error Handling**: Standardized error responses

## Technology Stack

- **Java 17** - Latest LTS version
- **Spring Boot 4.0.5** - Modern Spring framework
- **Spring Data JPA** - Object-relational mapping
- **PostgreSQL** - Primary database
- **Flyway** - Database migrations
- **SpringDoc OpenAPI 2.0.2** - API documentation (Swagger)
- **Jakarta Validation** - Request validation
- **Lombok** - Code generation

## Quick Start

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

## Project Structure

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
## License:
Apache 2.0