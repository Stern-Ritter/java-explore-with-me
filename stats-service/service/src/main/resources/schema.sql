DROP TABLE IF EXISTS requests_history;

CREATE TABLE IF NOT EXISTS requests_history (
    id BIGSERIAL,
    application_name VARCHAR(255) NOT NULL,
    uri VARCHAR(255) NOT NULL,
    ip VARCHAR(255) NOT NULL,
    requested_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_request_history PRIMARY KEY (id)
);