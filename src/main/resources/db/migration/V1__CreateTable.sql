CREATE TABLE IF NOT EXISTS report_entity (
    id UUID,
    description VARCHAR(255),
    user_id VARCHAR(255),
    request TEXT,
    create_date timestamp,
    PRIMARY KEY (id)
);