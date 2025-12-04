package com.streamify.backend;

import com.streamify.backend.dto.UpdateUserRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ContentService {
    private final ContentRepository contentRepository;

    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public boolean login(String email, String password) {
        return contentRepository.login(email, password);
    }

    public Map<String, Object> getMemberByEmail(String email) {
        Map<String, Object> memberDetails = contentRepository.getMemberByEmail(email);
        if (memberDetails != null && memberDetails.containsKey("isAdmin")) {
            Object isAdminValue = memberDetails.get("isAdmin");
            if (isAdminValue instanceof Number) {
                boolean isAdmin = ((Number) isAdminValue).intValue() == 1;
                memberDetails.put("isAdmin", isAdmin);
            }
        }
        return memberDetails;
    }

    public List<Map<String, Object>> browseMovies(String genre, String actor, String director, String keyword, Boolean awardWinning) {
        return contentRepository.browseMovies(genre, actor, director, keyword, awardWinning);
    }

    public List<Map<String, Object>> browseSeries(String genre, String actor, String director, String keyword, Boolean awardWinning, String email) {
        return contentRepository.browseSeries(genre, actor, director, keyword, awardWinning, email);
    }

    public List<Map<String, Object>> streamingHistory(String email) {
        return contentRepository.streamingHistory(email);
    }

    public Map<String, Object> getMovieDetails(String content_id) {
        Map<String, Object> movieDetails = new HashMap<>();
        movieDetails.put("details", contentRepository.getMovieDetails(content_id));
        movieDetails.put("sequels", contentRepository.getMovieSequels(content_id));
        return movieDetails;
    }

    public Map<String, Object> getSeriesDetails(String content_id) {
        Map<String, Object> seriesDetails = new HashMap<>();
        seriesDetails.put("details", contentRepository.getSeriesDetails(content_id));
        List<Map<String, Object>> seasons = contentRepository.getSeriesSeasons(content_id);

        Map<Object, List<Map<String, Object>>> groupedSeasons = seasons.stream()
                .collect(Collectors.groupingBy(m -> m.get("season_number")));

        groupedSeasons.forEach((seasonNumber, episodes) -> episodes.forEach(episode -> episode.remove("season_number")));

        seriesDetails.put("content", groupedSeasons);
        seriesDetails.put("num_seasons", groupedSeasons.size());
        return seriesDetails;
    }

    public List<Map<String, Object>> membersWhoStreamed(String content_id) {
        return contentRepository.membersWhoStreamed(content_id);
    }

    public List<Map<String, Object>> allContent(){
        return contentRepository.allContent();
    }

    public List<Map<String, Object>> last24hTrends() {
        return contentRepository.last24hTrends();
    }

    public List<Map<String, Object>> topTenStreamed() {
        return contentRepository.topTenStreamed();
    }

    public List<Map<String, Object>> getAllMembers() {
        return contentRepository.getAllMembers();
    }

    // Transactions
    @Transactional
    public void addMember(String email, String password, String name, String street,
                          String city, String state, String country, String phone, String subName){
        String member_id = nextId("member_id","member","M");

        String subscription_id = contentRepository.findSubscriptionIdByName(subName);

        contentRepository.insertUser(email, password, name, street, city, state, country, phone);
        contentRepository.insertMember(email, member_id, subscription_id);
    }

    @Transactional
    public void addMovie(String content_name, String release_date,
                         String IMDB_link, String genre, String poster_url, String sequel_to){

        String content_id = nextId("content_id","content","C");

        contentRepository.insertContent(content_id, content_name, release_date, IMDB_link, genre,
                poster_url);
        contentRepository.insertMovie(content_id, sequel_to);
    }

    @Transactional
    public void addSeries(String content_name, String release_date,
                          String IMDB_link, String genre, String poster_url,
                          String total_episodes, String total_seasons){

        String content_id = nextId("content_id","content","C");

        contentRepository.insertContent(content_id, content_name, release_date, IMDB_link, genre,
                poster_url);
        contentRepository.insertSeries(content_id, total_episodes, total_seasons);
    }

    @Transactional
    public void addEpisode(String content_id, String season_number,
                           String episode_number, String title, String release_date){
        String episode_id = nextEpId(content_id);

        contentRepository.insertEpisode(content_id,episode_id, season_number, episode_number, title, release_date);
    }

    @Transactional
    public void addToCurrentlyStreaming(String email, String content_id){
        String stream_id = nextId("stream_id","has","S");
        contentRepository.insertHas(stream_id, email, content_id);
    }

    @Transactional
    public void deleteMember(String email, String member_id){
        contentRepository.deleteMember(email, member_id);
        contentRepository.deleteUser(email);
    }

    @Transactional
    public void deleteMovie(String content_id){
        contentRepository.deleteMovie(content_id);
        contentRepository.deleteContent(content_id);
    }

    @Transactional
    public void deleteSeries(String content_id){
        contentRepository.deleteEpisode(content_id);
        contentRepository.deleteSeries(content_id);
        contentRepository.deleteContent(content_id);
    }

    @Transactional
    public void deleteEpisode(String content_id, String episode_id){
        contentRepository.deleteEpisode(content_id, episode_id);
    }

    @Transactional
    public void deleteFromCurrentlyStreaming(String stream_id, String email, String content_id){
        contentRepository.deleteHas(stream_id, email, content_id);
    }

    @Transactional
    public void updateUser(UpdateUserRequest request) {
        contentRepository.updateUser(request);
    }

    public String nextId(String primaryKey, String relation, String prefix){
        String maxId = contentRepository.findMaxLock(primaryKey, relation);
        int nextIdNum = 1;
        if (maxId != null){
            String leadingZeros = maxId.substring(1);
            int num = Integer.parseInt(leadingZeros);
            nextIdNum = num + 1;
        }
        return prefix + String.format("%04d", nextIdNum);
    }

    public String nextEpId(String content_id){
        String maxId = contentRepository.findEpMaxLock(content_id);
        int nextIdNum = 1;
        if (maxId != null){
            String leadingZeros = maxId.substring(1);
            int num = Integer.parseInt(leadingZeros);
            nextIdNum = num + 1;
        }
        return "E" + String.format("%04d", nextIdNum);
    }
}
