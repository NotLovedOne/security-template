CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE user_roles
(
    user_id INTEGER      NOT NULL,
    role    VARCHAR(255) NOT NULL
);

CREATE TABLE users
(
    id       INTEGER      NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    email    VARCHAR(255),
    creator  VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_on_user FOREIGN KEY (user_id) REFERENCES users (id);

-- Insert default admin user
INSERT INTO users (id, username, password, email)
VALUES (nextval('users_seq'), 'admin',
        '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', -- password: 'password'
        'admin@example.com');

-- Insert roles for admin user
INSERT INTO user_roles (user_id, role) VALUES
((SELECT id FROM users WHERE username = 'admin'), 'ROLE_ADMIN'),
((SELECT id FROM users WHERE username = 'admin'), 'ROLE_USER');

-- Create default roles data (can be used for reference when assigning roles to users)
CREATE TABLE IF NOT EXISTS roles (
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    CONSTRAINT pk_roles PRIMARY KEY (name)
);

-- Insert available roles
INSERT INTO roles (name, description) VALUES
('ROLE_USER', 'Standard user with basic privileges'),
('ROLE_ADMIN', 'Administrator with full access to all features'),
('ROLE_YERA', 'Custom role for specific system operations');
