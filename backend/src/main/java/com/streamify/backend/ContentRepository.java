package com.streamify.backend;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class ContentRepository {
    private final JdbcTemplate jdbcTemplate;

    public ContentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ---------------USER QUERIES---------------
    public boolean login(String email, String password) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND password = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{email, password}, Integer.class);
        return count != null && count > 0;
    }

    // Browse movies by keyword, actor, director, genre
    public List<Map<String, Object>> browseMovies(String genre, String actor, String director, String keyword, Boolean awardWinning) {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT c.content_id, c.content_name, c.poster_url " +
                "FROM content c " +
                "JOIN movie m ON c.content_id = m.content_id " +
                "LEFT JOIN castIn ci ON c.content_id = ci.content_id " +
                "LEFT JOIN directedBy db ON c.content_id = db.content_id " +
                "LEFT JOIN person actor ON ci.person_id = actor.person_id " +
                "LEFT JOIN person director ON db.person_id = director.person_id ");

        if (Boolean.TRUE.equals(awardWinning)) {
            sql.append("JOIN awardedTo at ON c.content_id = at.content_id ");
        }

        sql.append("WHERE c.genre LIKE CONCAT('%', ?, '%') " +
                "AND actor.name LIKE CONCAT('%', ?, '%') " +
                "AND director.name LIKE CONCAT('%', ?, '%') " +
                "AND c.content_name LIKE CONCAT('%', ?, '%')");

        return jdbcTemplate.queryForList(sql.toString(),
                genre != null ? genre : "",
                actor != null ? actor : "",
                director != null ? director : "",
                keyword != null ? keyword : "");
    }

    // Browse series by keyword, actor, director, genre
    public List<Map<String, Object>> browseSeries(String genre, String actor, String director, String keyword, Boolean awardWinning) {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT c.content_id, c.content_name, c.release_date, s.total_seasons, s.total_episodes, c.poster_url, " +
                "e.title AS episode_title, e.season_number, e.episode_number, e.release_date AS episode_release_date, c.IMDB_link, c.genre " +
                "FROM content c " +
                "JOIN series s ON c.content_id = s.content_id " +
                "LEFT JOIN episode e ON c.content_id = e.content_id " +
                "LEFT JOIN castIn ci ON c.content_id = ci.content_id " +
                "LEFT JOIN directedBy db ON c.content_id = db.content_id " +
                "LEFT JOIN person actor ON ci.person_id = actor.person_id " +
                "LEFT JOIN person director ON db.person_id = director.person_id ");

        if (Boolean.TRUE.equals(awardWinning)) {
            sql.append("JOIN awardedTo at ON c.content_id = at.content_id ");
        }

        sql.append("WHERE c.genre LIKE CONCAT('%', ?, '%') " +
                "  AND actor.name LIKE CONCAT('%', ?, '%') " +
                "  AND director.name LIKE CONCAT('%', ?, '%') " +
                "  AND c.content_name LIKE CONCAT('%', ?, '%')");

        return jdbcTemplate.queryForList(sql.toString(),
                genre != null ? genre : "",
                actor != null ? actor : "",
                director != null ? director : "",
                keyword != null ? keyword : "");
    }

    //browse series never streamed before
    public List<Map<String, Object>> notStreamedSeries(String email) {
        String sql = "SELECT DISTINCT c.content_id, c.content_name, c.release_date, s.total_seasons, s.total_episodes, " +
                "e.title AS episode_title, e.season_number, e.episode_number, e.release_date AS episode_release_date, c.IMDB_link, c.genre " +
                "FROM content c " +
                "JOIN series s ON c.content_id = s.content_id " +
                "LEFT JOIN episode e ON c.content_id = e.content_id " +
                "WHERE c.content_id NOT IN(" +
                    "SELECT content_id " +
                    "FROM streamingHistory " +
                    "WHERE email = ?" +
                ")";

        return jdbcTemplate.queryForList(sql, email);
    }

    //get streaming history
    public List<Map<String, Object>> streamingHistory(String email) {
        String sql = "SELECT c.content_name, sh.episode_id, sh.timestamp " +
                "FROM streamingHistory sh " +
                "JOIN content c ON sh.content_id = c.content_id " +
                "WHERE sh.email = ? " +
                "ORDER BY sh.timestamp DESC";

        return jdbcTemplate.queryForList(sql, email);
    }

    public List<Map<String, Object>> getMovieDetails(String content_id) {
        String sql = "SELECT c.poster_url, c.IMDB_link, c.content_name, c.release_date, c.genre, " +
                "(SELECT GROUP_CONCAT(p.name SEPARATOR ', ') FROM person p JOIN directedBy db ON p.person_id = db.person_id WHERE db.content_id = c.content_id) AS director, " +
                "(SELECT GROUP_CONCAT(p.name SEPARATOR ', ') FROM person p JOIN castIn ci ON p.person_id = ci.person_id WHERE ci.content_id = c.content_id) AS cast, " +
                "(SELECT GROUP_CONCAT(a.award_name SEPARATOR ', ') FROM award a JOIN awardedTo at ON a.award_name = at.award_name WHERE at.content_id = c.content_id) AS awards " +
                "FROM content c " +
                "WHERE c.content_id = ?";
        return jdbcTemplate.queryForList(sql, content_id);
    }

    public List<Map<String, Object>> getMovieSequels(String content_id) {
        String sql = "WITH RECURSIVE sequels_cte AS ( " +
                "  SELECT content_id, sequel_to, 1 AS level " +
                "  FROM movie " +
                "  WHERE content_id = ? " +
                "  UNION ALL " +
                "  SELECT m.content_id, m.sequel_to, s.level + 1 " +
                "  FROM movie m " +
                "  JOIN sequels_cte s ON m.sequel_to = s.content_id " +
                ") " +
                "SELECT c.content_id, c.poster_url " +
                "FROM sequels_cte s " +
                "JOIN content c ON s.content_id = c.content_id " +
                "WHERE s.level > 1";
        return jdbcTemplate.queryForList(sql, content_id);
    }

    //--------------ADMIN QUERIES------------------
    //get members who streamed a specific type of content
    public List<Map<String, Object>> membersWhoStreamed(String content_id) {
        String sql = "SELECT m.member_id, u.name " +
                "FROM streamingHistory sh " +
                "JOIN member m ON sh.email = m.email " +
                "JOIN users u ON m.email = u.email " +
                "WHERE sh.content_id = ?";

        return jdbcTemplate.queryForList(sql, content_id);
    }

    //get last 24 hours of streaming trends
    public List<Map<String, Object>> last24hTrends() {
        String sql = "SELECT sh.stream_id, c.content_id, c.content_name, sh.email, sh.timestamp " +
                "FROM streamingHistory sh " +
                "JOIN content c ON sh.content_id = c.content_id " +
                "WHERE sh.timestamp >= NOW() - INTERVAL 1 DAY " +
                "ORDER BY sh.timestamp DESC";

        return jdbcTemplate.queryForList(sql);
    }

    //get top ten streamed content
    public List<Map<String, Object>> topTenStreamed() {
        String sql = "SELECT c.content_id, c.content_name, COUNT(*) AS stream_count " +
                "FROM streamingHistory sh " +
                "JOIN content c ON sh.content_id = c.content_id " +
                "WHERE sh.timestamp >= NOW() - INTERVAL 1 MONTH " +
                "GROUP BY c.content_id, c.content_name " +
                "ORDER BY stream_count DESC " +
                "LIMIT 10";

        return jdbcTemplate.queryForList(sql);
    }

    //get all members
    public List<Map<String, Object>> getAllMembers() {
        String sql = "SELECT m.member_id, u.name, u.email, s.name AS subscription_name " +
                "FROM member m " +
                "JOIN users u ON m.email = u.email " +
                "LEFT JOIN subscriptionPlan s ON m.subscription_id = s.subscription_id";
        return jdbcTemplate.queryForList(sql);
    }
}
