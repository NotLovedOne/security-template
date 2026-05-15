CREATE TABLE Tickets (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'OPEN',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reporter_id BIGINT REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE tickets_assignees(
    ticket_id BIGINT REFERENCES Tickets(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (ticket_id, user_id)
)