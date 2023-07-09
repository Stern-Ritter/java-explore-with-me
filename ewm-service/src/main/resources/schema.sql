DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilations_events CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT unique_email_users UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT unique_name_categories UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS locations (
    id BIGSERIAL,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL,
    CONSTRAINT pk_locations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events (
    id BIGSERIAL,
    creation_date TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    publication_date TIMESTAMP WITHOUT TIME ZONE,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    title VARCHAR(120) NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    description VARCHAR(7000) NOT NULL,
    is_paid BOOLEAN NOT NULL DEFAULT FALSE,
    is_moderation_required BOOLEAN NOT NULL DEFAULT TRUE,
    participant_limit INT NOT NULL DEFAULT 0,
    state VARCHAR(32) NOT NULL,
    category_id BIGINT NOT NULL,
    initiator_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT events_to_categories_fk FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT events_to_users_fk FOREIGN KEY (initiator_id) REFERENCES users (id),
    CONSTRAINT events_to_locations_fk FOREIGN KEY (location_id) REFERENCES locations (id)
);

CREATE TABLE IF NOT EXISTS events_likes (
    event_id BIGINT,
    user_id BIGINT,
    CONSTRAINT pk_events_likes PRIMARY KEY (event_id, user_id),
    CONSTRAINT events_likes_to_events_fk FOREIGN KEY (event_id) REFERENCES events(id),
    CONSTRAINT events_likes_to_users_fk FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS events_dislikes (
    event_id BIGINT,
    user_id BIGINT,
    CONSTRAINT pk_events_dislikes PRIMARY KEY (event_id, user_id),
    CONSTRAINT events_likes_to_events_fk FOREIGN KEY (event_id) REFERENCES events(id),
    CONSTRAINT events_likes_to_users_fk FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS requests (
    id BIGSERIAL,
    creation_date TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    status VARCHAR(32) NOT NULL,
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT unique_event_id_requester_id UNIQUE (event_id, requester_id),
    CONSTRAINT requests_to_events_fk FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT requests_to_users_fk FOREIGN KEY (requester_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS compilations (
    id BIGSERIAL,
    title VARCHAR(255) NOT NULL,
    is_pinned BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_compilations PRIMARY KEY (id),
    CONSTRAINT unique_compilation_title UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS compilations_events (
    compilation_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    CONSTRAINT pk_compilations_events PRIMARY KEY (compilation_id, event_id),
    CONSTRAINT compilations_events_to_events_fk FOREIGN KEY (event_id) REFERENCES events (id)
);
