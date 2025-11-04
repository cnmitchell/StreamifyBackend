package com.streamify.backend;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class ContentService {
    private final ContentRepository contentRepository;

    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public List<Map<String, Object>> browseMovies(String genre, String actor, String director, String keyword) {
        return contentRepository.browseMovies(genre, actor, director, keyword);
    }

    public List<Map<String, Object>> browseSeries(String genre, String actor, String director, String keyword) {
        return contentRepository.browseSeries(genre, actor, director, keyword);
    }

    public List<Map<String, Object>> awardWinningMovies() {
        return contentRepository.awardWinningMovies();
    }

    public List<Map<String, Object>> notStreamedSeries(String email) {
        return contentRepository.notStreamedSeries(email);
    }

    public List<Map<String, Object>> streamingHistory(String email) {
        return contentRepository.streamingHistory(email);
    }

    public List<Map<String, Object>> sequels(String content_id) {
        return contentRepository.sequels(content_id);
    }

    public List<Map<String, Object>> membersWhoStreamed(String content_id) {
        return contentRepository.membersWhoStreamed(content_id);
    }

    public List<Map<String, Object>> last24hTrends() {
        return contentRepository.last24hTrends();
    }

    public List<Map<String, Object>> topTenStreamed() {
        return contentRepository.topTenStreamed();
    }
}
