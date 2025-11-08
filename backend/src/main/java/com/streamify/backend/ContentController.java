package com.streamify.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/content")
public class ContentController {
    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping("/browse/movies")
    public List<Map<String, Object>> browseMovies(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String actor,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean awardWinning) {
        return contentService.browseMovies(genre, actor, director, keyword, awardWinning);
    }

    @GetMapping("/browse/series") //need to add series to db
    public List<Map<String, Object>> browseSeries(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String actor,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean awardWinning) {
        return contentService.browseSeries(genre, actor, director, keyword, awardWinning);
    }

    @GetMapping("/browse/series/not-streamed") //need to add series to db
    public List<Map<String, Object>> notStreamedSeries(@RequestParam String email) {
        return contentService.notStreamedSeries(email);
    }

    @GetMapping("/streaming-history")
    public List<Map<String, Object>> streamingHistory(@RequestParam String email) {
        return contentService.streamingHistory(email);
    }

    @GetMapping("/movie-details")
    public Map<String, Object> getMovieDetails(@RequestParam String content_id) {
        return contentService.getMovieDetails(content_id);
    }

    @GetMapping("/members-who-streamed") //getting empty array back??
    public List<Map<String, Object>> membersWhoStreamed(@RequestParam String content_id) {
        return contentService.membersWhoStreamed(content_id);
    }

    @GetMapping("/last-24h-trends") //need to test again
    public List<Map<String, Object>> last24hTrends() {
        return contentService.last24hTrends();
    }

    @GetMapping("/top-ten-streamed")
    public List<Map<String, Object>> topTenStreamed() {
        return contentService.topTenStreamed();
    }

    @GetMapping("/members")
    public List<Map<String, Object>> getAllMembers() {
        return contentService.getAllMembers();
    }
}