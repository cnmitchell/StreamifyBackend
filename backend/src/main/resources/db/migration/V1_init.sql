CREATE TABLE users
(
    email VARCHAR(20) PRIMARY KEY,
    password VARCHAR(20),
    name VARCHAR(20),
    street VARCHAR(20),
    city VARCHAR(20),
    state VARCHAR(20),
    country VARCHAR(20),
    phone VARCHAR(20)
);

CREATE TABLE content
(
    content_id CHAR(20) PRIMARY KEY,
    release_date VARCHAR(20),
    IMDB_link VARCHAR(20),
    genre VARCHAR(20)
);

CREATE TABLE subscriptionPlan
(
    subscription_id CHAR(20) PRIMARY KEY,
    name VARCHAR(20),
    active_streams INT,
    price INT
);

CREATE TABLE member
(
    email VARCHAR(20),
    member_id CHAR(20),
    subscription_id CHAR(20),
    PRIMARY KEY (email,member_id),
    FOREIGN KEY(subscription_id) REFERENCES subscriptionPlan(subscription_id),
    FOREIGN KEY (email) REFERENCES users(email)
);

CREATE TABLE admin
(
    email VARCHAR(20),
    admin_id CHAR(20),
    PRIMARY KEY (email,admin_id),
    FOREIGN KEY (email) REFERENCES users(email)
);

CREATE TABLE movie
(
    content_id CHAR(20) PRIMARY KEY,
    sequel_to CHAR(20),
    FOREIGN KEY (content_id) REFERENCES content(content_id),
    FOREIGN KEY (sequel_to) REFERENCES movie(content_id)
);

CREATE TABLE series
(
    content_id CHAR(20) PRIMARY KEY,
    total_episodes INT,
    total_seasons INT,
    FOREIGN KEY (content_id) REFERENCES content(content_id)
);

CREATE TABLE episode
(
    content_id CHAR(20),
    episode_id CHAR(20),
    season_number INT,
    episode_number INT,
    title VARCHAR(20),
    release_date DATE,
    PRIMARY KEY (content_id, episode_id),
    FOREIGN KEY (content_id) REFERENCES content(content_id)
);

CREATE TABLE stream
(
    email CHAR(20),
    content_id CHAR(20),
    PRIMARY KEY(email,content_id),
    FOREIGN KEY (email) REFERENCES users(email),
    FOREIGN KEY (content_id) REFERENCES content(content_id)
);

CREATE TABLE person
(
    person_id CHAR(20) PRIMARY KEY,
    name VARCHAR(20),
    state VARCHAR(20),
    country VARCHAR(20)
);

CREATE TABLE award
(
    award_name VARCHAR(20) PRIMARY KEY
);

CREATE TABLE awardedTo
(
    award_name VARCHAR(20),
    content_id CHAR(20),
    PRIMARY KEY (award_name, content_id),
    FOREIGN KEY (award_name) REFERENCES award(award_name),
    FOREIGN KEY (content_id) REFERENCES content(content_id)
);

CREATE TABLE castIn
(
    content_id CHAR(20),
    person_id CHAR(20),
    PRIMARY KEY(content_id, person_id),
    FOREIGN KEY (content_id) REFERENCES content(content_id),
    FOREIGN KEY (person_id) REFERENCES person(person_id)
);

CREATE TABLE streamingHistory
(
    stream_id CHAR(20) PRIMARY KEY,
    email CHAR(20),
    content_id CHAR(20),
    episode_id CHAR(20),
    timestamp DATETIME,
    FOREIGN KEY (email) REFERENCES users(email),
    FOREIGN KEY (content_id) REFERENCES content(content_id)
);

CREATE TABLE has
(
    stream_id CHAR(20),
    email CHAR(20),
    content_id CHAR(20),
    PRIMARY KEY (stream_id, email, content_id),
    FOREIGN KEY (content_id) REFERENCES content(content_id),
    FOREIGN KEY (email) REFERENCES users(email),
    FOREIGN KEY (stream_id) REFERENCES streamingHistory(stream_id)
);

CREATE TABLE directedBy
(
    content_id CHAR(20),
    person_id CHAR(20),
    PRIMARY KEY (content_id, person_id),
    FOREIGN KEY (content_id) REFERENCES content(content_id),
    FOREIGN KEY (person_id) REFERENCES person(person_id)
);