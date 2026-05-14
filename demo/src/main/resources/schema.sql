CREATE TABLE wallet (
    msisdn VARCHAR(15) PRIMARY KEY,
    balance NUMERIC(12,2) NOT NULL DEFAULT 0 CHECK (balance >= 0)
);