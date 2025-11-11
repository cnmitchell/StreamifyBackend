package com.streamify.backend;

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

        groupedSeasons.forEach((seasonNumber, episodes) -> {
            episodes.forEach(episode -> episode.remove("season_number"));
        });

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
}
