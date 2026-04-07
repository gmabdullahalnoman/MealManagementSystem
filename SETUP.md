# Setup Guide - Meal Management System

## 🔧 Local Development Environment Setup

### Step 1: Prerequisites Installation

#### Windows
1. **Install Java 17+**
   - Download from: https://www.oracle.com/java/technologies/downloads/#java17
   - Or use chocolatey: `choco install openjdk17`

2. **Install PostgreSQL**
   - Download from: https://www.postgresql.org/download/windows/
   - Set password for `postgres` user (remember this!)
   - Default port: 5432

3. **Install Maven (optional, project includes mvnw)**
   - Download from: https://maven.apache.org/download.cgi

#### macOS
```bash
# Using Homebrew
brew install openjdk@17
brew install postgresql@15
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt-get update
sudo apt-get install openjdk-17-jdk postgresql postgresql-contrib
```

### Step 2: PostgreSQL Configuration

#### Create Database
```bash
# Connect to PostgreSQL
psql -U postgres

# In PostgreSQL prompt
CREATE DATABASE mealmanagementdb;
\q
```

#### Verify Connection
```bash
psql -U postgres -d mealmanagementdb -c "SELECT version();"
```

### Step 3: Application Configuration

#### Option A: Use Environment Variables (Recommended for Security) ⭐

Set these before running the application:

```bash
# Windows (Command Prompt)
set DB_URL=jdbc:postgresql://localhost:5432/mealmanagementdb
set DB_USER=postgres
set DB_PASSWORD=your_postgres_password

# Windows (PowerShell)
$env:DB_URL="jdbc:postgresql://localhost:5432/mealmanagementdb"
$env:DB_USER="postgres"
$env:DB_PASSWORD="your_postgres_password"

# Linux/macOS
export DB_URL=jdbc:postgresql://localhost:5432/mealmanagementdb
export DB_USER=postgres
export DB_PASSWORD=your_postgres_password
```

#### Option B: Create Development Profile (For Local Use Only)

1. Copy the example file:
```bash
cp src/main/resources/application-dev.properties.example src/main/resources/application-dev.properties
```

2. Edit `src/main/resources/application-dev.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mealmanagementdb
spring.datasource.username=postgres
spring.datasource.password=your_postgres_password
```

3. Run with dev profile:
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

**⚠️ Important:** `application-dev.properties` is already in `.gitignore` - never commit it!

#### Configuration Priority

The application loads properties in this order:
1. `application.properties` (base config with env variables)
2. `application-dev.properties` (if running with `--spring.profiles.active=dev`)
3. Environment variables (override everything)

### Step 4: Build & Run

```bash
# Navigate to project directory
cd MealManagementSystem

# Clean and build (this runs all checks)
./mvnw clean install

# Run the application
./mvnw spring-boot:run

# Alternative: Run from JAR
./mvnw clean package
java -jar target/MealManagementSystem-0.0.1-SNAPSHOT.jar
```

### Step 5: Verify Installation

Open browser and go to:
- **API Root**: http://localhost:8080/api/sessions
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/v3/api-docs

## 🐛 Troubleshooting

### PostgreSQL Connection Issues

**Error: "Connection refused"**
```
Solution:
1. Verify PostgreSQL is running
   - Windows: Services → PostgreSQL
   - Linux/Mac: brew services list | grep postgres
2. Check database exists: psql -U postgres -l
```

**Error: "FATAL: password authentication failed"**
```
Solution:
1. Reset password: psql -U postgres -c "ALTER ROLE postgres PASSWORD 'new_password';"
2. Update application.properties with new password
```

### Build Issues

**Error: "mvnw: command not found"**
```bash
# Windows: Use mvnw.cmd instead
./mvnw.cmd clean install
```

**Error: Java version mismatch**
```bash
# Check Java version
java -version

# Should show version 17 or higher
# If not, set JAVA_HOME environment variable
```

### Application Won't Start

1. **Check logs for exact error**
2. **Verify database is accessible**
3. **Ensure port 8080 is available**
   - Windows: `netstat -ano | findstr 8080`
   - Linux/Mac: `lsof -i :8080`

## 📦 Docker Setup (Optional)

### Using Docker Compose

Create `docker-compose.yml`:

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: mealmanagementdb
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

Start PostgreSQL with Docker:
```bash
docker-compose up -d
```

## 🔐 Production Setup

### Environment-Specific Configuration

Create `application-prod.properties`:

```properties
spring.datasource.url=jdbc:postgresql://prod-db-host:5432/mealmanagementdb
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate
logging.level.root=WARN
logging.level.com.mealmanager=INFO
```

Run with:
```bash
java -jar app.jar --spring.profiles.active=prod
```

### Database Backup

```bash
# Backup database
pg_dump -U postgres mealmanagementdb > backup.sql

# Restore database
psql -U postgres mealmanagementdb < backup.sql
```

## ✅ Validation Checklist

- [ ] Java 17+ installed
- [ ] PostgreSQL running
- [ ] Database created
- [ ] application.properties configured
- [ ] Project builds successfully
- [ ] Application starts without errors
- [ ] Swagger UI accessible
- [ ] Can call API endpoints

---

For more help, check the main [README.md](README.md)
