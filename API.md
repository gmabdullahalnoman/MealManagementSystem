# API Reference - Meal Management System

## 📌 Overview

All API endpoints return standardized responses in this format:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": { /* actual data */ },
  "timestamp": "2026-04-07T10:30:00"
}
```

## Members API

### GET /api/members
List all members

**Response:**
```json
{
  "success": true,
  "message": "Members retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "John Doe",
      "phone": "01711234567",
      "isActive": true,
      "createdAt": "2026-04-07T10:00:00"
    }
  ]
}
```

### GET /api/members/{id}
Get specific member

**Parameters:**
- `id` (path): Member ID

**Response:**
```json
{
  "success": true,
  "message": "Member retrieved successfully",
  "data": {
    "id": 1,
    "name": "John Doe",
    "phone": "01711234567",
    "isActive": true
  }
}
```

### GET /api/members/active
List active members only

### POST /api/members
Create new member

**Request Body:**
```json
{
  "name": "John Doe",
  "phone": "01711234567"
}
```

**Validation Rules:**
- `name`: Required, non-blank
- `phone`: Optional, must be 11 digits if provided

### PUT /api/members/{id}
Update member

**Request Body:**
```json
{
  "name": "Jane Doe",
  "phone": "01811234567"
}
```

### PATCH /api/members/{id}/activate
Activate a deactivated member

### PATCH /api/members/{id}/deactivate
Deactivate a member (keeps history)

---

## Sessions API

### POST /api/sessions
Create new session

**Request Body:**
```json
{
  "name": "April 2026",
  "startDate": "2026-04-01",
  "endDate": "2026-04-30"
}
```

**Validation Rules:**
- `name`: Required, non-blank
- `startDate`: Required, valid date
- `endDate`: Optional, must be >= startDate

### GET /api/sessions
List all sessions

### GET /api/sessions/{id}
Get session details

### GET /api/sessions/active
Get currently active session

**Response:**
```json
{
  "success": true,
  "message": "Active session retrieved successfully",
  "data": {
    "id": 1,
    "name": "April 2026",
    "startDate": "2026-04-01",
    "isActive": true,
    "isClosed": false
  }
}
```

### POST /api/sessions/{id}/close
Close a session (finalizes accounting)

---

## Deposits API

### POST /api/deposits
Record a deposit

**Request Body:**
```json
{
  "sessionId": 1,
  "memberId": 1,
  "amount": 5000,
  "depositDate": "2026-04-07",
  "type": "REGULAR",
  "note": "Initial deposit"
}
```

**Validation Rules:**
- `sessionId`: Required
- `memberId`: Required
- `amount`: Required, must be positive
- `depositDate`: Required
- `type`: Optional (default: "REGULAR")

### GET /api/deposits/session/{sessionId}
Get all deposits for a session

### GET /api/deposits/session/{sessionId}/total
Get total deposits for a session

**Response:**
```json
{
  "success": true,
  "message": "Total deposits calculated successfully",
  "data": 25000
}
```

### GET /api/deposits/member/{memberId}
Get all deposits by a member

### GET /api/deposits/session/{sessionId}/member/{memberId}/total
Get total deposits by a member in a session

### DELETE /api/deposits/{id}
Delete a deposit record

---

## Expenses API

### POST /api/expenses
Record an expense

**Request Body:**
```json
{
  "sessionId": 1,
  "expenseDate": "2026-04-07",
  "amount": 2500,
  "description": "Groceries",
  "memberId": 1
}
```

**Validation Rules:**
- `sessionId`: Required
- `expenseDate`: Required
- `amount`: Required, must be positive
- `description`: Required, non-blank
- `memberId`: Optional

### GET /api/expenses/session/{sessionId}
Get all expenses for a session

### GET /api/expenses/session/{sessionId}/total
Get total expenses for a session

### GET /api/expenses/date-range
Get expenses within date range

**Query Parameters:**
- `startDate`: Start date (yyyy-MM-dd)
- `endDate`: End date (yyyy-MM-dd)

### PUT /api/expenses/{id}
Update an expense

**Request Body:**
```json
{
  "amount": 3000,
  "description": "Groceries and supplies"
}
```

### DELETE /api/expenses/{id}
Delete an expense

---

## Meals API

### POST /api/meals
Record a meal

**Request Body:**
```json
{
  "sessionId": 1,
  "memberId": 1,
  "mealDate": "2026-04-07",
  "mealType": "LUNCH",
  "guestCount": 0,
  "hostMemberId": 1
}
```

**Validation Rules:**
- `sessionId`: Required
- `memberId`: Required
- `mealDate`: Required
- `mealType`: Required (BREAKFAST, LUNCH, DINNER, etc.)
- `guestCount`: Optional (default: 0)

### GET /api/meals/session/{sessionId}
Get all meals in a session

### GET /api/meals/session/{sessionId}/date/{date}
Get meals on a specific date

**Parameters:**
- `date`: Date in yyyy-MM-dd format

### GET /api/meals/session/{sessionId}/total
Get total meal count

### GET /api/meals/session/{sessionId}/per-member
Get meal count per member

### PUT /api/meals/{id}
Update a meal

**Request Body:**
```json
{
  "mealType": "LUNCH",
  "guestCount": 2
}
```

### DELETE /api/meals/{id}
Delete a meal record

---

## Reports API

### GET /api/reports/session/{sessionId}
Get complete report for a session

**Response includes:**
- Session details
- Total deposits
- Total expenses
- Meal calculations
- Per-member settlement

### GET /api/reports/active-session
Get report for currently active session

### GET /api/reports/closed-months
List all closed session summaries

### POST /api/reports/close/{sessionId}
Close a session and settle accounts

**Response includes:**
- Final settlement amounts
- Who owes whom
- New session created with carry-forward balances

### GET /api/reports/closed/{sessionId}
Get report of a closed session

---

## Error Responses

### Validation Error (400)
```json
{
  "success": false,
  "status": 400,
  "message": "Member name is required",
  "error": "Validation Failed",
  "timestamp": "2026-04-07T10:30:00",
  "path": "/api/members"
}
```

### Not Found (404)
```json
{
  "status": 404,
  "message": "Member with ID 999 not found",
  "error": "Resource Not Found",
  "timestamp": "2026-04-07T10:30:00",
  "path": "/api/members/999"
}
```

### Conflict (409)
```json
{
  "status": 409,
  "message": "Cannot close an already closed session",
  "error": "Invalid Operation",
  "timestamp": "2026-04-07T10:30:00",
  "path": "/api/sessions/1/close"
}
```

---

## Common Response Codes

- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `400 Bad Request` - Validation or invalid input
- `404 Not Found` - Resource not found
- `409 Conflict` - Business logic conflict
- `500 Internal Server Error` - Server error

---

## Rate Limiting

Currently, no rate limiting is implemented. Each endpoint can be called as frequently as needed.

## Authentication

Authentication is not currently enabled. All endpoints are publicly accessible.

*Future versions may add JWT authentication.*

---

For more details, visit Swagger UI at `/swagger-ui.html`
