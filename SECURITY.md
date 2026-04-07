# Security Guide - Meal Management System

## 🔒 Overview

This guide outlines security best practices for the Meal Management System REST API, ensuring that sensitive information is never exposed.

---

## ⚠️ Files Never to Commit

### Never Push These Files/Folders

```
❌ Never Commit:
- application-dev.properties          (local database credentials)
- application-local.properties        (local overrides)
- application-prod.properties         (production secrets)
- .env                               (environment variables)
- .env.local                         (local environment variables)
- .aws/credentials                   (AWS credentials)
- Any file with actual passwords     (database, API keys, tokens)
```

### Safe to Commit

```
✅ Always Safe:
- application.properties             (uses environment variables)
- application-dev.properties.example  (template only, no secrets)
- pom.xml                           (dependencies, no credentials)
- .gitignore                        (ignore rules)
- src/                              (source code)
- README.md, SETUP.md               (documentation)
```

---

## 🔑 Managing Database Credentials

### Option 1: Environment Variables (Recommended)

Set before running the application:

```bash
# Linux/macOS
export DB_URL=jdbc:postgresql://localhost:5432/mealmanagementdb
export DB_USER=postgres
export DB_PASSWORD=your_secure_password

# Windows PowerShell
$env:DB_URL="jdbc:postgresql://localhost:5432/mealmanagementdb"
$env:DB_USER="postgres"
$env:DB_PASSWORD="your_secure_password"

# Windows Command Prompt
set DB_URL=jdbc:postgresql://localhost:5432/mealmanagementdb
set DB_USER=postgres
set DB_PASSWORD=your_secure_password
```

Then run:
```bash
./mvnw spring-boot:run
```

### Option 2: Development Profile

**For local development only:**

1. Create `src/main/resources/application-dev.properties` (NOT in git):
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mealmanagementdb
spring.datasource.username=postgres
spring.datasource.password=postgres
```

2. Add to `.gitignore` (already done):
```
application-dev.properties
```

3. Run with dev profile:
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### Option 3: System Environment Variables (Production)

Use container orchestration or deployment tools to inject:

```yaml
# Docker example
environment:
  DB_URL: jdbc:postgresql://prod-db:5432/mealmanagementdb
  DB_USER: ${PROD_DB_USER}
  DB_PASSWORD: ${PROD_DB_PASSWORD}
```

---

## 🛡️ Security Checklist Before Pushing

- [ ] No passwords in `application.properties`
- [ ] `application-dev.properties` is in `.gitignore`
- [ ] `.env` file is in `.gitignore`
- [ ] No API keys in code or config files
- [ ] Database credentials use environment variables
- [ ] Reviewed all `.properties` files for secrets
- [ ] Checked `.java` files for hardcoded credentials

---

## 🚨 If Sensitive Data Was Committed

If you accidentally committed secrets to GitHub:

1. **Immediately rotate the credentials** (change database password, revoke API keys, etc.)

2. **Remove from git history:**
```bash
# Using BFG Repo-Cleaner (recommended)
bfg --replace-text passwords.txt

# Or using git filter-branch
git filter-branch --force --index-filter \
  'git rm --cached --ignore-unmatch src/main/resources/application-dev.properties' \
  --prune-empty --tag-name-filter cat -- --all
```

3. **Force push to remove history:**
```bash
git push origin --force --all
```

---

## 🔐 Production Deployment

### Best Practices

1. **Use Container Secrets**
   - Docker: Use `--env-file` or environment variables
   - Kubernetes: Use Secrets objects

2. **Use Secret Management Tools**
   - AWS Secrets Manager
   - HashiCorp Vault
   - Azure Key Vault
   - Spring Cloud Config Server

3. **Environment Variables**
   ```bash
   export DB_URL=jdbc:postgresql://prod-host:5432/db
   export DB_USER=prod_user
   export DB_PASSWORD=$(aws secretsmanager get-secret-value --secret-id db-password)
   ```

4. **Never Use File-Based Secrets in Containers**

---

## 📋 .gitignore Configuration

Current `.gitignore` includes:

```
# Build artifacts
target/

# IDE settings
.vscode/
.idea/

# Environment and configuration
.env
.env.local
application-*.properties

# Logs
*.log

# OS files
.DS_Store
Thumbs.db
```

---

## 🔍 Scanning for Secrets

Use tools to detect accidentally committed secrets:

```bash
# Using git-secrets
git secrets --scan

# Using detect-secrets
detect-secrets scan

# Using truffleHog
truffleHog --entropy=True --regex
```

---

## 📞 Security Issues

If you discover a security vulnerability:

1. **Do NOT post publicly** on GitHub Issues
2. **Contact maintainers privately**
3. **Allow time for patching** before disclosure

---

## Quick Reference

| Scenario | Action |
|----------|--------|
| Local development | Use `application-dev.properties` (not committed) |
| Testing | Use environment variables |
| Production | Use container secrets or secret management service |
| Shared credentials | Create `.env` file (add to .gitignore) |
| CI/CD environment | Use GitHub Secrets or GitLab CI/CD variables |

---

For more security best practices, visit:
- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [Spring Security Guide](https://spring.io/projects/spring-security)
- [GitHub Security Advisories](https://github.com/advisories)
