# User Browsing - Movies
# By keyword/search
SELECT DISTINCT c.content_id, c.content_name, c.release_date, c.IMDB_link, c.genre
FROM content c JOIN movie m ON c.content_id = m.content_id
JOIN castIn i ON c.content_id = i.content_id
JOIN directedBy d ON c.content_id = d.content_id
JOIN person p1 ON i.person_id = p1.person_id
JOIN person p2 ON d.person_id = p2.person_id
WHERE
c.genre LIKE CONCAT('%','%')
AND
p1.name LIKE CONCAT('%','%') # actor
AND
p2.name LIKE CONCAT('%','%') # director.
AND
c.content_name LIKE CONCAT('%','%');

# By award-winning
SELECT DISTINCT c.content_id, c.content_name, c.release_date, c.IMDB_link, c.genre, a.award_name
FROM content c JOIN movie m ON c.content_id = m.content_id
JOIN awardedTo a ON c.content_id = a.content_id;

# User browsing, by series + episodes, keyword/search
SELECT DISTINCT c.content_id, c.content_name, c.release_date, s.total_seasons, s.total_episodes,
                e.title, e.season_number, e.episode_number, e.release_date, c.IMDB_link, c.genre
FROM content c JOIN series s ON c.content_id = s.content_id
                JOIN episode e ON c.content_id = e.content_id
               JOIN castIn i ON c.content_id = i.content_id
               JOIN directedBy d ON c.content_id = d.content_id
               JOIN person p1 ON i.person_id = p1.person_id
               JOIN person p2 ON d.person_id = p2.person_id
WHERE
    c.genre LIKE CONCAT('%','%')
  AND
    p1.name LIKE CONCAT('%','%') # actor
  AND
    p2.name LIKE CONCAT('%','%') # director
  AND
    c.content_name LIKE CONCAT('%','%');

# Get series not streamed before
SELECT DISTINCT c.content_id, c.content_name, c.release_date, s.total_seasons, s.total_episodes,
                e.title, e.season_number, e.episode_number, e.release_date, c.IMDB_link, c.genre
FROM content c
JOIN series s ON c.content_id = s.content_id
JOIN episode e ON c.content_id = e.content_id
WHERE c.content_id NOT IN(
    SELECT content_id
    FROM streamingHistory
    WHERE email = ?
    );

# Get top ten streamed content.
SELECT content_id
FROM streamingHistory s JOIN content c ON s.content_id = c.content_id
WHERE timestamp >= NOW() - INTERVAL 1 MONTH
GROUP BY content_id
ORDER BY COUNT(*) DESC
LIMIT 10;

# Get last 24 hours of streaming trends.
SELECT s.stream_id, c.content_id, c.content_name, s.email, s.timestamp
FROM streamingHistory s JOIN content c ON s.content_id = c.content_id
WHERE timestamp >= NOW() - INTERVAL 1 DAY;

# Get members who streamed a specific type of content.
SELECT m.member_id, u.name
FROM stream s JOIN member m ON s.email = m.email
JOIN users u ON m.email = u.email
WHERE content_id = ?;

# Get streaming history of a user.
SELECT c.content_name, timestamp
FROM streamingHistory s JOIN content c ON c.content_id = s.content_id
WHERE email = ?;

# Get all sequels to a movie.
SELECT c.content_name
FROM movie m JOIN content c ON m.content_id = c.content_id
WHERE sequel_to = ?;
