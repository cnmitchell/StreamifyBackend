package com.streamify.backend;

import com.streamify.backend.dto.AddFullContentRequest;
import com.streamify.backend.dto.AwardRequest;
import com.streamify.backend.dto.EpisodeRequest;
import com.streamify.backend.dto.PersonRequest;
import com.streamify.backend.dto.UpdateUserRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ContentService {
    private static final Logger logger = LoggerFactory.getLogger(ContentService.class);

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

    public List<PersonRequest> searchPeople(String name) {
        List<Map<String, Object>> peopleMaps = contentRepository.searchPeopleByName(name);
        return peopleMaps.stream().map(map -> {
            PersonRequest person = new PersonRequest();
            person.setName((String) map.get("name"));
            person.setState(Objects.toString(map.get("state"), null));
            person.setCountry(Objects.toString(map.get("country"), null));
            return person;
        }).collect(Collectors.toList());
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
    public void addEpisode(String content_id, int season_number,
                           int episode_number, String title){
        String episode_id = nextEpId(content_id);

        contentRepository.insertEpisode(content_id,episode_id, season_number, episode_number, title);
    }

    @Transactional
    public void addToCurrentlyStreaming(String email, String content_id){
        String stream_id = nextId("stream_id","has","S");
        contentRepository.insertHas(stream_id, email, content_id);
    }

    @Transactional
    public void addFullContent(AddFullContentRequest request) {
        System.out.println("DEBUG: Entering addFullContent method.");
        System.out.println("DEBUG: Content Name: " + request.getContent_name());
        System.out.println("DEBUG: Release Date: " + request.getRelease_date());
        System.out.println("DEBUG: IMDB Link: " + request.getIMDB_link());
        System.out.println("DEBUG: Genre: " + request.getGenre());
        System.out.println("DEBUG: Poster URL: " + request.getPoster_url());
        System.out.println("DEBUG: Sequel To: " + request.getSequel_to());
        System.out.println("DEBUG: Total Episodes: " + request.getTotal_episodes());
        System.out.println("DEBUG: Total Seasons: " + request.getTotal_seasons());

        String contentId = nextId("content_id", "content", "C");
        contentRepository.insertContent(contentId, request.getContent_name(), request.getRelease_date(),
                request.getIMDB_link(), request.getGenre(), request.getPoster_url());

        if (request.getTotal_episodes() != null || request.getTotal_seasons() != null) {
            contentRepository.insertSeries(contentId, request.getTotal_episodes(), request.getTotal_seasons());
            if (request.getEpisodes() != null) {
                for (EpisodeRequest episode : request.getEpisodes()) {
                    addEpisode(contentId, episode.getSeason_number(), episode.getEpisode_number(),
                            episode.getTitle());
                }
            }
        } else {
            contentRepository.insertMovie(contentId, request.getSequel_to());
        }

        for (PersonRequest person : request.getCast()) {
            String personId = contentRepository.findPersonIdByName(person.getName());
            if (personId == null) {
                personId = nextId("person_id", "person", "P");
                contentRepository.insertPerson(personId, person.getName(), person.getState(), person.getCountry());
            }
            contentRepository.insertCastIn(contentId, personId);
        }

        for (PersonRequest person : request.getDirectors()) {
            String personId = contentRepository.findPersonIdByName(person.getName());
            if (personId == null) {
                personId = nextId("person_id", "person", "P");
                contentRepository.insertPerson(personId, person.getName(), person.getState(), person.getCountry());
            }
            contentRepository.insertDirectedBy(contentId, personId);
        }

        for (AwardRequest award : request.getAwards()) {
            contentRepository.insertAward(award.getAward_name());
            contentRepository.insertAwardedTo(contentId, award.getAward_name(), award.getAward_year());
        }
        System.out.println("DEBUG: Exiting addFullContent method.");
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
    public void deleteContent(String content_id) {
        if (contentRepository.isSeries(content_id)) {
            deleteSeries(content_id);
        } else {
            deleteMovie(content_id);
        }
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
