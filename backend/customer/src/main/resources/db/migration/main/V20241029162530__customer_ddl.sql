CREATE TABLE customer (
    id VARCHAR PRIMARY KEY,                     -- Integer, unique identifier for each record
    name VARCHAR(255) NOT NULL,                -- Name, required
    email VARCHAR(255) UNIQUE NOT NULL,        -- Email, required and unique, email format
    phone VARCHAR(10) CHECK (phone ~ '^[0-9]{10}$'), -- Phone, optional with 10-digit pattern
    gender VARCHAR(10) CHECK (gender IN ('Male', 'Female', 'Other')), -- Enum-like constraint for gender
    order_id VARCHAR(255),                     -- Order ID, optional
    transaction_id VARCHAR(255),               -- Transaction ID, optional
    created_at TIMESTAMP DEFAULT NOW(),        -- Created at, optional with default timestamp
    updated_at TIMESTAMP DEFAULT NOW(),        -- Updated at, optional with default timestamp
    submission_id VARCHAR(255),                -- Optional string for submission ID
    content_id VARCHAR(255)                    -- Optional string for content ID
);
