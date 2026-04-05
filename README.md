# MealManagementSystem
A system for bachelor or medium enterprises to manage meals and calculations.

# Meal Management System (MMS)

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

### Next version (v0.0.3) planned:
- All Service layer implementations
- Business logic for:
  - Meal rate calculation (total expense ÷ total meals)
  - Member balance calculation
  - Month closing logic
  - Balance carry forward
  - Validation rules

### Tech Stack:
- Spring Boot 4.0.5
- Spring Data JPA
- Thymeleaf
- H2 Database (dev)
- Maven

### How to run:
```bash
mvn spring-boot:run

Then visit: http://localhost:8080

License:
Apache 2.0