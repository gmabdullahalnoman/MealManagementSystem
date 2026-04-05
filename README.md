# MealManagementSystem
A system for bachelor or medium enterprises to manage meals and calculations.

## Version: 0.0.0 - Project Structure Foundation

### What's in this version:
- Spring Boot 4.0.5 project initialized
- Maven build configuration
- H2 database configured for development
- Project structure created with:
  - Controller layer (6 controllers)
  - Service layer (7 services)
  - Repository layer (8 repositories)
  - Entity layer (8 entities)
  - DTO layer (6 DTOs)
  - Configuration layer
  - Frontend templates (6 HTML pages)
  - Static assets (CSS/JS)

### Version: 0.0.1 - All Entities Completed
- Session entity implementation
- Member entity implementation
- Deposit entity implementation
- Expense entity implementation
- MemberOpeningBalance entity implementation
- MemberClosedBalance entity implementation
- ClosedMonthSummary entity implementation
- Complete all the entities

## Version: 0.0.2 - All Repositories Completed
- SessionRepository (JPA with custom queries)
- MemberRepository (active/inactive member queries)
- DepositRepository (sum queries by session/member)
- ExpenseRepository (sum queries by session/date range)
- MealRecordRepository (meal calculation queries, guest counting)
- MemberOpeningBalanceRepository (carry forward balance queries)
- ClosedMonthSummaryRepository (month-end summary queries)
- MemberClosedBalanceRepository (per-member balance queries)

### Version: 0.0.3 - All Service Completed
- All Service layer implementations
- Business logic for:
  - Meal rate calculation (total expense ÷ total meals)
  - Member balance calculation
  - Month closing logic
  - Balance carry forward
  - Validation rules

## Version: 0.0.4 - All Controllers Completed (REST APIs + Thymeleaf Views)
- **SessionController** - Session management (create, close, list) with REST APIs
- **MemberController** - Member CRUD, activate/deactivate with REST APIs
- **DepositController** - Deposit entry, totals, delete with REST APIs
- **ExpenseController** - Expense entry, totals, update/delete with REST APIs
- **MealController** - Meal entry, duplicate prevention, guest tracking with REST APIs
- **ReportController** - Session reports, month closing, balance carry forward with REST APIs

## Version: 0.0.5 - Complete Frontend & Dashboard

- ✅ All 8 Entities with JPA mappings
- ✅ All 8 Repositories with custom queries
- ✅ All 7 Services with business logic
- ✅ All 6 Controllers (REST APIs + Thymeleaf views)
- ✅ Complete Frontend with Bootstrap 5
- ✅ Dashboard with real-time statistics
- ✅ Member management (CRUD with soft delete)
- ✅ Deposit & Expense tracking
- ✅ Meal entry with duplicate prevention
- ✅ Guest meal handling with host member
- ✅ Month closing with balance carry forward
- ✅ Report generation (active & closed months)
### Current Status: 🚧 Being Debugged - Issues Being Fixed:
- 🐛 Template fragment resolution (layout inheritance)
- 🐛 Dashboard data binding

### API Endpoints Available:
- GET/POST/PUT/DELETE endpoints for all entities
- Real-time calculation endpoints
- Report generation endpoints
- Month closing endpoint with automatic carry forward

### Tech Stack:
- Spring Boot 4.0.5
- Spring Data JPA
- Thymeleaf
- H2 Database (dev)
- Maven

## How to Run

### Prerequisites:
- JDK 17
- Maven (or use wrapper)

### Steps:
```bash
# Clone repository
git clone https://github.com/YOUR_USERNAME/MealManagementSystem.git

# Navigate to project
cd MealManagementSystem

# Run the application
.\mvnw spring-boot:run   (Windows)
./mvnw spring-boot:run   (Mac/Linux)

# Open browser
http://localhost:8080

Features Implemented
Feature	                        Status
Session/Month Management	      ✅
Member CRUD with Soft Delete  	✅
Deposit Tracking	              ✅
Expense Tracking	              ✅
Daily Meal Entry	              ✅
Duplicate Meal Prevention	      ✅
Guest Meal with Host Member	    ✅
Real-time Meal Rate Calculation	✅
Member Balance Calculation	    ✅
Month Closing	                  ✅
Balance Carry Forward	          ✅
Dashboard with Stats	          ✅
Reports (Active & Closed)	      ✅
Print Reports

License:
Apache 2.0