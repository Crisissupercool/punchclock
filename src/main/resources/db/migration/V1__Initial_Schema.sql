-- Initial schema creation for Punchclock application

-- Create employee table
CREATE TABLE employee (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

-- Create category table
CREATE TABLE category (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

-- Create tag table
CREATE TABLE tag (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

-- Create entry table
CREATE TABLE entry (
    id BIGSERIAL PRIMARY KEY,
    check_in TIMESTAMP NOT NULL,
    check_out TIMESTAMP,
    employee_id BIGINT NOT NULL REFERENCES employee(id),
    category_id BIGINT REFERENCES category(id)
);

-- Create entry_tag join table (ManyToMany)
CREATE TABLE entry_tag (
    entry_id BIGINT NOT NULL REFERENCES entry(id),
    tag_id BIGINT NOT NULL REFERENCES tag(id),
    PRIMARY KEY (entry_id, tag_id)
);

-- Create application_user table
CREATE TABLE application_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    employee_id BIGINT UNIQUE REFERENCES employee(id)
);

-- Create indexes for better performance
CREATE INDEX idx_entry_employee ON entry(employee_id);
CREATE INDEX idx_entry_category ON entry(category_id);
CREATE INDEX idx_entry_checkin ON entry(check_in);
CREATE INDEX idx_application_user_username ON application_user(username);
