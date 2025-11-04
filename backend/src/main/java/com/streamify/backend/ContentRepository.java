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
    // Browse movies by keyword, actor, director, genre
    public List<Map<String, Object>> browseMovies(String genre, String actor, String director, String keyword) {
        String sql = "SELECT DISTINCT " +
                "c.content_id, c.content_name, c.release_date, c.IMDB_link, c.genre " +
                "FROM content c " +
                "JOIN movie m ON c.content_id = m.content_id " +
                "LEFT JOIN castIn ci ON c.content_id = ci.content_id " +
                "LEFT JOIN directedBy db ON c.content_id = db.content_id " +
                "LEFT JOIN person actor ON ci.person_id = actor.person_id " +
                "LEFT JOIN person director ON db.person_id = director.person_id " +
                "WHERE c.genre LIKE CONCAT('%', ?, '%') " +
                "AND actor.name LIKE CONCAT('%', ?, '%') " +
                "AND director.name LIKE CONCAT('%', ?, '%') " +
                "AND c.content_name LIKE CONCAT('%', ?, '%')";

        return jdbcTemplate.queryForList(sql,
                genre != null? genre : "",
                actor != null? actor : "",
                director != null? director : "",
                keyword != null? keyword : "");
    }

    // Browse series by keyword, actor, director, genre
    public List<Map<String, Object>> browseSeries(String genre, String actor, String director, String keyword) {
        String sql = "SELECT DISTINCT c.content_id, c.content_name, c.release_date, s.total_seasons, s.total_episodes, " +
                "e.title AS episode_title, e.season_number, e.episode_number, e.release_date AS episode_release_date, c.IMDB_link, c.genre " +
                "FROM content c " +
                "JOIN series s ON c.content_id = s.content_id " +
                "LEFT JOIN episode e ON c.content_id = e.content_id " +
                "LEFT JOIN castIn ci ON c.content_id = ci.content_id " +
                "LEFT JOIN directedBy db ON c.content_id = db.content_id " +
                "LEFT JOIN person actor ON ci.person_id = actor.person_id " +
                "LEFT JOIN person director ON db.person_id = director.person_id " +
                "WHERE c.genre LIKE CONCAT('%', ?, '%') " +
                "  AND actor.name LIKE CONCAT('%', ?, '%') " +
                "  AND director.name LIKE CONCAT('%', ?, '%') " +
                "  AND c.content_name LIKE CONCAT('%', ?, '%')";

        return jdbcTemplate.queryForList(sql,
                genre != null? genre : "",
                actor != null? actor : "",
                director != null? director : "",
                keyword != null? keyword : "");
    }

    //browse award-winning movies
    public List<Map<String, Object>> awardWinningMovies() {
        String sql = "SELECT DISTINCT c.content_id, c.content_name, c.release_date, c.IMDB_link, c.genre, a.award_name " +
                "FROM content c " +
                "JOIN movie m ON c.content_id = m.content_id " +
                "JOIN awardedTo at ON c.content_id = at.content_id " +
                "JOIN award a ON at.award_name = a.award_name";

        return jdbcTemplate.queryForList(sql);
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

    //get all sequels to a movie
    public List<Map<String, Object>> sequels(String content_id) {
        String sql = "SELECT c.content_name, m.content_id, m.sequel_to " +
                "FROM movie m " +
                "JOIN content c ON m.content_id = c.content_id " +
                "WHERE m.sequel_to = ?";

        return jdbcTemplate.queryForList(sql, content_id);
    }

    //--------------ADMIN QUERIES------------------
    //get members who streamed a specific type of content
    public List<Map<String, Object>> membersWhoStreamed(String content_id) {
        String sql = "SELECT m.member_id, u.name " +
                "FROM stream s " +
                "JOIN member m ON s.email = m.email " +
                "JOIN users u ON m.email = u.email " +
                "WHERE s.content_id = ?";

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

}
