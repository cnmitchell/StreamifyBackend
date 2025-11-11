-- Add Member
START TRANSACTION;
INSERT INTO users (email, password, name, street, city, state, country, phone)
VALUES (?,?,?,?,?,?,?,?);
INSERT INTO member (email, member_id) VALUES (?,?);
COMMIT;

-- Remove Member
START TRANSACTION;
DELETE FROM member WHERE email = ? and member_id = ?;
DELETE FROM users WHERE email = ?;
COMMIT;

-- Add Movie
START TRANSACTION;
INSERT INTO content (content_id, content_name, release_date, IMDB_link, genre, poster_url)
VALUES (?, ?, ?, ?, ?, ?);
INSERT INTO movie (content_id, sequel_to) VALUES (?, ?);
COMMIT;

-- Remove Movie
START TRANSACTION;
DELETE FROM movie WHERE content_id = ?;
DELETE FROM content WHERE content_id = ?;
COMMIT;

-- Add Series
START TRANSACTION;
INSERT INTO content (content_id, content_name, release_date, IMDB_link, genre, poster_url)
VALUES (?, ?, ?, ?, ?, ?);
INSERT INTO series (content_id, total_episodes, total_seasons) VALUES (?,?,?);
COMMIT;

-- Remove Series
START TRANSACTION;
DELETE FROM episode WHERE content_id = ?;
DELETE FROM series WHERE content_id = ?;
DELETE FROM content WHERE content_id = ?;
COMMIT;

-- Add Episode
START TRANSACTION;
INSERT INTO episode (content_id, episode_id, season_number, episode_number, title, release_date)
VALUES (?,?,?,?,?,?);
COMMIT;

-- Remove Episode
START TRANSACTION;
DELETE FROM episode WHERE content_id = ? AND episode_id = ?;
COMMIT;
-- Modify Member Transactions

-- Change Email
START TRANSACTION;
UPDATE users
SET email = ?
WHERE email = ?;

UPDATE member
SET email = ?
WHERE email = ?;
COMMIT;

-- Change member subscription
START TRANSACTION;
UPDATE member
SET subscription_id = ?
WHERE email = ?;
COMMIT;

-- Add to currently streaming
START TRANSACTION;
INSERT INTO has (stream_id, email, content_id) VALUES (?,?,?);
COMMIT;

-- Delete from currently streaming (trigger handles adding to history)
START TRANSACTION;
DELETE FROM has WHERE stream_id = ? AND email = ? AND content_id = ?;
COMMIT;

