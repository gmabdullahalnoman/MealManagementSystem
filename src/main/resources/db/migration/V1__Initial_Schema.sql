-- V1__Initial_Schema.sql
-- Initial database schema for Meal Management System

-- Create Members table
CREATE TABLE IF NOT EXISTS members (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(name, is_active)
);

-- Create Sessions table
CREATE TABLE IF NOT EXISTS sessions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    is_active BOOLEAN DEFAULT TRUE,
    is_closed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_dates CHECK (end_date IS NULL OR start_date <= end_date)
);

-- Create Deposits table
CREATE TABLE IF NOT EXISTS deposits (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    session_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    amount DOUBLE NOT NULL,
    deposit_date DATE NOT NULL,
    type VARCHAR(20) DEFAULT 'REGULAR',
    note TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES sessions(id),
    FOREIGN KEY (member_id) REFERENCES members(id),
    CONSTRAINT chk_amount CHECK (amount > 0)
);

-- Create Expenses table
CREATE TABLE IF NOT EXISTS expenses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    session_id BIGINT NOT NULL,
    member_id BIGINT,
    expense_date DATE NOT NULL,
    amount DOUBLE NOT NULL,
    description VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES sessions(id),
    FOREIGN KEY (member_id) REFERENCES members(id),
    CONSTRAINT chk_exp_amount CHECK (amount > 0)
);

-- Create Meal Records table
CREATE TABLE IF NOT EXISTS meal_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    session_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    meal_date DATE NOT NULL,
    meal_type VARCHAR(20) NOT NULL,
    guest_count INT DEFAULT 0,
    host_member_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES sessions(id),
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (host_member_id) REFERENCES members(id),
    UNIQUE(session_id, member_id, meal_date, meal_type)
);

-- Create Member Opening Balance table
CREATE TABLE IF NOT EXISTS member_opening_balances (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    session_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    opening_balance DOUBLE NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES sessions(id),
    FOREIGN KEY (member_id) REFERENCES members(id),
    UNIQUE(session_id, member_id)
);

-- Create Member Closed Balance table
CREATE TABLE IF NOT EXISTS member_closed_balances (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    session_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    total_deposit DOUBLE NOT NULL,
    total_meals INT NOT NULL,
    total_cost DOUBLE NOT NULL,
    balance DOUBLE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES sessions(id),
    FOREIGN KEY (member_id) REFERENCES members(id)
);

-- Create Closed Month Summary table
CREATE TABLE IF NOT EXISTS closed_month_summaries (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    session_id BIGINT NOT NULL UNIQUE,
    total_deposit DOUBLE NOT NULL,
    total_expense DOUBLE NOT NULL,
    total_meals INT NOT NULL,
    meal_rate DOUBLE NOT NULL,
    closed_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES sessions(id)
);

-- Create indexes for better query performance
CREATE INDEX idx_deposits_session ON deposits(session_id);
CREATE INDEX idx_deposits_member ON deposits(member_id);
CREATE INDEX idx_expenses_session ON expenses(session_id);
CREATE INDEX idx_expenses_date ON expenses(expense_date);
CREATE INDEX idx_meals_session ON meal_records(session_id);
CREATE INDEX idx_meals_date ON meal_records(meal_date);
CREATE INDEX idx_sessions_active ON sessions(is_active);
CREATE INDEX idx_sessions_closed ON sessions(is_closed);
CREATE INDEX idx_members_active ON members(is_active);
