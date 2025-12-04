package com.streamify.backend;

import com.streamify.backend.dto.UpdateUserRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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

    public Map<String, Object> getMemberByEmail(String email) {
        String sql = "SELECT u.email, u.name, u.street, u.city, u.state, u.country, u.phone, m.member_id, s.name AS subscription_name, s.subscription_id " +
                "FROM users u " +
                "LEFT JOIN member m ON u.email = m.email " +
                "LEFT JOIN subscriptionPlan s ON m.subscription_id = s.subscription_id " +
                "WHERE u.email = ?";
        try {
            return jdbcTemplate.queryForMap(sql, email);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
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
    public List<Map<String, Object>> browseSeries(String genre, String actor, String director, String keyword, Boolean awardWinning, String email) {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT c.content_id, c.content_name, c.poster_url " +
                "FROM content c " +
                "JOIN series s ON c.content_id = s.content_id " +
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

        List<Object> params = new ArrayList<>();
        params.add(genre != null ? genre : "");
        params.add(actor != null ? actor : "");
        params.add(director != null ? director : "");
        params.add(keyword != null ? keyword : "");

        if (email != null && !email.isEmpty()) {
            sql.append(" AND c.content_id NOT IN (SELECT content_id FROM streamingHistory WHERE email = ?)");
            params.add(email);
        }

        return jdbcTemplate.queryForList(sql.toString(), params.toArray());
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

    public List<Map<String, Object>> getSeriesDetails(String content_id) {
        String sql = "SELECT c.poster_url, c.IMDB_link, c.content_name, c.release_date, c.genre, " +
                "(SELECT GROUP_CONCAT(p.name SEPARATOR ', ') FROM person p JOIN directedBy db ON p.person_id = db.person_id WHERE db.content_id = c.content_id) AS director, " +
                "(SELECT GROUP_CONCAT(p.name SEPARATOR ', ') FROM person p JOIN castIn ci ON p.person_id = ci.person_id WHERE ci.content_id = c.content_id) AS cast, " +
                "(SELECT GROUP_CONCAT(a.award_name SEPARATOR ', ') FROM award a JOIN awardedTo at ON a.award_name = at.award_name WHERE at.content_id = c.content_id) AS awards " +
                "FROM content c " +
                "WHERE c.content_id = ?";
        return jdbcTemplate.queryForList(sql, content_id);
    }

    public List<Map<String, Object>> getSeriesSeasons(String content_id) {
        String sql = "SELECT season_number, episode_number, title " +
                "FROM episode " +
                "WHERE content_id = ? " +
                "ORDER BY season_number, episode_number";
        return jdbcTemplate.queryForList(sql, content_id);
    }

    //--------------ADMIN QUERIES------------------
    //get members who streamed a specific type of content
    public List<Map<String, Object>> membersWhoStreamed(String content_id) {
        String sql = "SELECT m.member_id, u.name, m.email, sh.timestamp " +
                "FROM streamingHistory sh " +
                "JOIN member m ON sh.email = m.email " +
                "JOIN users u ON m.email = u.email " +
                "WHERE sh.content_id = ? " +
                "ORDER BY sh.timestamp DESC";

        return jdbcTemplate.queryForList(sql, content_id);
    }

    public List<Map<String, Object>> allContent(){
        String sql = "SELECT c.content_id, c.content_name " +
                "FROM content c";

        return jdbcTemplate.queryForList(sql);
    }

    //get last 24 hours of streaming trends commented out to test last 24 streamed
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
        String sql = "SELECT c.content_id, c.genre, c.release_date, c.content_name, COUNT(*) AS stream_count " +
                "FROM streamingHistory sh " +
                "JOIN content c ON sh.content_id = c.content_id " +
                "WHERE sh.timestamp >= NOW() - INTERVAL 1 MONTH " +
                "GROUP BY c.content_id, c.content_name, c.genre, c.release_date " +
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

    // Transactions
    public void insertUser(String email, String password, String name, String street,
                          String city, String state, String country, String phone) {
        String sql = "INSERT INTO users (email, password, name, street, city, state, country, phone) " +
                "VALUES (?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, email, password, name, street, city, state, country, phone);
    }

    public void insertMember(String email, String member_id, String subscription_id) {
        String sql = "INSERT INTO member (email, member_id, subscription_id) " +
                "VALUES (?,?,?)";
        jdbcTemplate.update(sql, email, member_id, subscription_id);
    }

    public void insertContent(String content_id, String content_name, String release_date,
                              String IMDB_link, String genre, String poster_url) {
        String sql = "INSERT INTO content (content_id, content_name, release_date," +
                "IMDB_link, genre, poster_url) VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(sql,content_id, content_name, release_date, IMDB_link, genre, poster_url);
    }

    public void insertMovie(String content_id, String sequel_to){
        String sql = "INSERT INTO movie(content_id, sequel_to) VALUES (?,?)";
        jdbcTemplate.update(sql, content_id, sequel_to);
    }

    public void insertSeries(String content_id, String total_episodes,String total_seasons){
        String sql = "INSERT INTO series(content_id, total_episodes, total_seasons) VALUES (?,?,?)";
        jdbcTemplate.update(sql, content_id, total_episodes, total_seasons);
    }

    public void insertEpisode(String content_id, String episode_id, String season_number,
                              String episode_number, String title, String release_date){
        String sql = "INSERT into episode(content_id, episode_id, season_number, episode_number, title, release_date) " +
                "VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(sql, content_id, episode_id, season_number, episode_number, title, release_date);
    }

    public void insertHas(String stream_id, String email, String content_id){
        String sql = "INSERT INTO has(stream_id, email, content_id) VALUES (?,?,?)";
        jdbcTemplate.update(sql, stream_id, email, content_id);
    }

    public void deleteMember(String email, String member_id){
        String sql = "DELETE FROM member WHERE email = ? AND member_id = ?";
        jdbcTemplate.update(sql, email, member_id);
    }

    public void deleteUser(String email){
        String sql = "DELETE FROM users WHERE email = ?";
        jdbcTemplate.update(sql, email);
    }

    public void deleteMovie(String content_id){
        String sql = "DELETE FROM movie WHERE content_id = ?";
        jdbcTemplate.update(sql, content_id);
    }

    public void deleteContent(String content_id){
        String sql = "DELETE FROM content WHERE content_id = ?";
        jdbcTemplate.update(sql, content_id);
    }

    public void deleteSeries(String content_id){
        String sql = "DELETE FROM series WHERE content_id = ?";
        jdbcTemplate.update(sql, content_id);
    }

    public void deleteEpisode(String content_id){
        String sql = "DELETE FROM episode WHERE content_id = ?";
        jdbcTemplate.update(sql, content_id);
    }

    public void deleteEpisode(String content_id, String episode_id){
        String sql = "DELETE FROM episode WHERE content_id = ? AND  episode_id = ?";
        jdbcTemplate.update(sql, content_id, episode_id);
    }

    public void deleteHas(String stream_id, String email, String content_id){
        String sql = "DELETE FROM has WHERE  stream_id = ? AND email = ? AND content_id = ?";
        jdbcTemplate.update(sql, stream_id, email, content_id);
    }

    public void updateUser(UpdateUserRequest request) {
        StringBuilder sql = new StringBuilder("UPDATE users SET ");
        List<Object> params = new ArrayList<>();

        if (request.getNewEmail() != null) {
            sql.append("email = ?, ");
            params.add(request.getNewEmail());
        }
        if (request.getPassword() != null) {
            sql.append("password = ?, ");
            params.add(request.getPassword());
        }
        if (request.getName() != null) {
            sql.append("name = ?, ");
            params.add(request.getName());
        }
        if (request.getStreet() != null) {
            sql.append("street = ?, ");
            params.add(request.getStreet());
        }
        if (request.getCity() != null) {
            sql.append("city = ?, ");
            params.add(request.getCity());
        }
        if (request.getState() != null) {
            sql.append("state = ?, ");
            params.add(request.getState());
        }
        if (request.getCountry() != null) {
            sql.append("country = ?, ");
            params.add(request.getCountry());
        }
        if (request.getPhone() != null) {
            sql.append("phone = ?, ");
            params.add(request.getPhone());
        }

        if (!params.isEmpty()) {
            sql.setLength(sql.length() - 2);
            sql.append(" WHERE email = ?");
            params.add(request.getEmail());
            jdbcTemplate.update(sql.toString(), params.toArray());
        }

        if (request.getNewEmail() != null) {
            String updateMemberSql = "UPDATE member SET email = ? WHERE email = ?";
            jdbcTemplate.update(updateMemberSql, request.getNewEmail(), request.getEmail());
        }

        if (request.getSubscriptionId() != null) {
            String updateSubscriptionSql = "UPDATE member SET subscription_id = ? WHERE email = ?";
            jdbcTemplate.update(updateSubscriptionSql, request.getSubscriptionId(), request.getNewEmail() != null ? request.getNewEmail() : request.getEmail());
        }
    }

    public String findMaxLock(String primaryKey, String relation){
        String sql = String.format(
                "SELECT %s FROM %s ORDER BY %s DESC LIMIT 1 FOR UPDATE",
                primaryKey,
                relation,
                primaryKey
        );
        try {
            return jdbcTemplate.queryForObject(sql, String.class);
        } catch (org.springframework.dao.EmptyResultDataAccessException e)
        {
            return null;
        }
    }

    public String findEpMaxLock(String primaryKey){
        String sql = "SELECT episode_id FROM episode " +
                "WHERE content_id = ? " +
                "ORDER BY episode_id DESC LIMIT 1 FOR UPDATE";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, primaryKey);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    public String findSubscriptionIdByName(String planName) {
        String sql = "SELECT subscription_id FROM subscriptionPlan WHERE name = ?";

        try {
            return jdbcTemplate.queryForObject(sql, String.class, planName);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }
}
