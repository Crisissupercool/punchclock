-- Initial schema creation for Punchclock application
-- This migration is designed to work with existing databases from drop-and-create mode

-- Create employee table (using old column names: firstname, lastname)
CREATE TABLE IF NOT EXISTS employee (
    id BIGSERIAL PRIMARY KEY,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

-- Create category table
CREATE TABLE IF NOT EXISTS category (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

-- Create tag table
CREATE TABLE IF NOT EXISTS tag (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

-- Create entry table (using old column names: checkin, checkout, without description)
CREATE TABLE IF NOT EXISTS entry (
    id BIGSERIAL PRIMARY KEY,
    checkin TIMESTAMP NOT NULL,
    checkout TIMESTAMP,
    employee_id BIGINT NOT NULL REFERENCES employee(id),
    category_id BIGINT REFERENCES category(id)
);

-- Create entry_tag join table (ManyToMany)
CREATE TABLE IF NOT EXISTS entry_tag (
    entry_id BIGINT NOT NULL REFERENCES entry(id),
    tag_id BIGINT NOT NULL REFERENCES tag(id),
    PRIMARY KEY (entry_id, tag_id)
);

-- Create application_user table
CREATE TABLE IF NOT EXISTS application_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    employee_id BIGINT UNIQUE REFERENCES employee(id)
);
