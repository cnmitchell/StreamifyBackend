DROP USER IF EXISTS 'streamify_user'@'%';
DROP USER IF EXISTS 'streamify_user'@'localhost';
DROP USER IF EXISTS 'streamify_user'@'127.0.0.1';

CREATE DATABASE IF NOT EXISTS streamify_db;

CREATE USER 'streamify_user'@'%' IDENTIFIED WITH mysql_native_password BY 'streamify_pass';
CREATE USER 'streamify_user'@'localhost' IDENTIFIED WITH mysql_native_password BY 'streamify_pass';
CREATE USER 'streamify_user'@'127.0.0.1' IDENTIFIED WITH mysql_native_password BY 'streamify_pass';

GRANT ALL PRIVILEGES ON streamify_db.* TO 'streamify_user'@'%';
GRANT ALL PRIVILEGES ON streamify_db.* TO 'streamify_user'@'localhost';
GRANT ALL PRIVILEGES ON streamify_db.* TO 'streamify_user'@'127.0.0.1';

FLUSH PRIVILEGES;

USE streamify_db;

CREATE TABLE users
(
    email VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255),
    name VARCHAR(50),
    street VARCHAR(100),
    city VARCHAR(50),
    state VARCHAR(50),
    country VARCHAR(50),
    phone VARCHAR(20)
);

CREATE TABLE content
(
    content_id CHAR(20) PRIMARY KEY,
    content_name VARCHAR(100),
    release_date DATE,
    IMDB_link VARCHAR(255),
    genre VARCHAR(50),
    poster_url VARCHAR(300)
);

CREATE TABLE subscriptionPlan
(
    subscription_id CHAR(20) PRIMARY KEY,
    name VARCHAR(50),
    active_streams INT,
    price DECIMAL(6,2)
);

CREATE TABLE member
(
    email VARCHAR(50),
    member_id CHAR(20),
    subscription_id CHAR(20),
    PRIMARY KEY (email,member_id),
    FOREIGN KEY(subscription_id) REFERENCES subscriptionPlan(subscription_id),
    FOREIGN KEY (email) REFERENCES users(email),
    INDEX idx_member_subscription(subscription_id)
);

CREATE TABLE admin
(
    email VARCHAR(50),
    admin_id CHAR(20),
    PRIMARY KEY (email,admin_id),
    FOREIGN KEY (email) REFERENCES users(email)
);

CREATE TABLE movie
(
    content_id CHAR(20) PRIMARY KEY,
    sequel_to CHAR(20),
    FOREIGN KEY (content_id) REFERENCES content(content_id),
    FOREIGN KEY (sequel_to) REFERENCES movie(content_id),
    INDEX idx_movie_sequel(sequel_to)
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
    title VARCHAR(100),
    release_date DATE,
    PRIMARY KEY (content_id, episode_id),
    FOREIGN KEY (content_id) REFERENCES content(content_id),
    INDEX idx_episode_season(content_id, season_number),
    INDEX idx_episode_number(content_id, season_number, episode_number)
);

CREATE TABLE stream
(
    email VARCHAR(50),
    content_id CHAR(20),
    PRIMARY KEY(email,content_id),
    FOREIGN KEY (email) REFERENCES users(email),
    FOREIGN KEY (content_id) REFERENCES content(content_id),
    INDEX idx_stream_content(content_id)
);

CREATE TABLE person
(
    person_id CHAR(20) PRIMARY KEY,
    name VARCHAR(100),
    state VARCHAR(50),
    country VARCHAR(50)
);

CREATE TABLE award
(
    award_name VARCHAR(100) PRIMARY KEY
);

CREATE TABLE awardedTo
(
    award_name VARCHAR(100),
    content_id CHAR(20),
    PRIMARY KEY (award_name, content_id),
    FOREIGN KEY (award_name) REFERENCES award(award_name),
    FOREIGN KEY (content_id) REFERENCES content(content_id),
    INDEX idx_awardedTo_content(content_id)
);

CREATE TABLE castIn
(
    content_id CHAR(20),
    person_id CHAR(20),
    PRIMARY KEY(content_id, person_id),
    FOREIGN KEY (content_id) REFERENCES content(content_id),
    FOREIGN KEY (person_id) REFERENCES person(person_id),
    INDEX idx_cast_person(person_id)
);

CREATE TABLE streamingHistory
(
    stream_id CHAR(20) PRIMARY KEY,
    email VARCHAR(50),
    content_id CHAR(20),
    episode_id CHAR(20),
    timestamp DATETIME,
    FOREIGN KEY (email) REFERENCES users(email),
    FOREIGN KEY (content_id) REFERENCES content(content_id),
    INDEX idx_history_email(email),
    INDEX idx_history_content(content_id),
    INDEX idx_history_time(timestamp)
);

CREATE TABLE has
(
    stream_id CHAR(20),
    email VARCHAR(50),
    content_id CHAR(20),
    PRIMARY KEY (stream_id, email, content_id),
    FOREIGN KEY (content_id) REFERENCES content(content_id),
    FOREIGN KEY (email) REFERENCES users(email),
    FOREIGN KEY (stream_id) REFERENCES streamingHistory(stream_id),
    INDEX idx_has_stream(stream_id),
    INDEX idx_has_email(email),
    INDEX idx_has_content(content_id)
);

CREATE TABLE directedBy
(
    content_id CHAR(20),
    person_id CHAR(20),
    PRIMARY KEY (content_id, person_id),
    FOREIGN KEY (content_id) REFERENCES content(content_id),
    FOREIGN KEY (person_id) REFERENCES person(person_id),
    INDEX idx_directed_person(person_id)
);