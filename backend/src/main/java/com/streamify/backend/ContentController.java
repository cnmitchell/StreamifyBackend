package com.streamify.backend;

import com.streamify.backend.dto.*;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/content")
public class ContentController {
    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = contentService.login(loginRequest.getEmail(), loginRequest.getPassword());
        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
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

    @GetMapping("/browse/series")
    public List<Map<String, Object>> browseSeries(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String actor,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean awardWinning,
            @RequestParam(required = false) String email) {
        return contentService.browseSeries(genre, actor, director, keyword, awardWinning, email);
    }

    @GetMapping("/streaming-history")
    public List<Map<String, Object>> streamingHistory(@RequestParam String email) {
        return contentService.streamingHistory(email);
    }

    @GetMapping("/movie-details")
    public Map<String, Object> getMovieDetails(@RequestParam String content_id) {
        return contentService.getMovieDetails(content_id);
    }

    @GetMapping("/series-details")
    public Map<String, Object> getSeriesDetails(@RequestParam String content_id) {
        return contentService.getSeriesDetails(content_id);
    }

    @GetMapping("/members-who-streamed") //getting empty array back??
    public List<Map<String, Object>> membersWhoStreamed(@RequestParam String content_id) {
        return contentService.membersWhoStreamed(content_id);
    }

    @GetMapping("/all-content")
    public List<Map<String, Object>> allContent() {
        return contentService.allContent();
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

    // Transactions

    @PostMapping("/member")
    public ResponseEntity<String> addMember(@RequestBody AddMemberRequest request) {
        try {
            contentService.addMember(
                    request.getEmail(),
                    request.getPassword(),
                    request.getName(),
                    request.getStreet(),
                    request.getCity(),
                    request.getState(),
                    request.getCountry(),
                    request.getPhone(),
                    request.getSubName()
            );
            return ResponseEntity.status(201).body("Member added.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/movie")
    public ResponseEntity<String> addMovie(@RequestBody AddMovieRequest request) {
        try {
            contentService.addMovie(
                    request.getContent_name(),
                    request.getRelease_date(),
                    request.getIMDB_link(),
                    request.getGenre(),
                    request.getPoster_url(),
                    request.getSequel_to()
            );
            return ResponseEntity.status(201).body("Movie added.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/series")
    public ResponseEntity<String> addSeries(@RequestBody AddSeriesRequest request) {
        try {
            contentService.addSeries(
                    request.getContent_name(),
                    request.getRelease_date(),
                    request.getIMDB_link(),
                    request.getGenre(),
                    request.getPoster_url(),
                    request.getTotal_episodes(),
                    request.getTotal_seasons()
            );
            return ResponseEntity.status(201).body("Series added.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/episode")
    public ResponseEntity<String> addEpisode(@RequestBody AddEpisodeRequest request) {
        try {
            contentService.addEpisode(
                    request.getContent_id(),
                    request.getSeason_number(),
                    request.getEpisode_number(),
                    request.getTitle(),
                    request.getRelease_date()
            );
            return ResponseEntity.status(201).body("Episode added.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/has")
    public ResponseEntity<String> addStreamed(@RequestBody AddStreamingRequest request) {
        try {
            contentService.addToCurrentlyStreaming(
                    request.getEmail(),
                    request.getContent_id()
            );
            return ResponseEntity.status(201).body("Stream added.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping("/member")
    public ResponseEntity<String> deleteMember(@RequestBody DeleteMemberRequest request) {
        try {
            contentService.deleteMember(
                    request.getEmail(),
                    request.getMember_id()
            );
            return ResponseEntity.status(204).body("Member deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping("/movie")
    public ResponseEntity<String> deleteMovie(@RequestBody DeleteMovieRequest request) {
        try {
            contentService.deleteMovie(
                    request.getContent_id()
            );
            return ResponseEntity.status(204).body("Movie deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping("/series")
    public ResponseEntity<String> deleteSeries(@RequestBody DeleteSeriesRequest request) {
        try {
            contentService.deleteSeries(
                    request.getContent_id()
            );
            return ResponseEntity.status(204).body("Series deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping("/episode")
    public ResponseEntity<String> deleteEpisode(@RequestBody DeleteEpisodeRequest request) {
        try {
            contentService.deleteEpisode(
                    request.getContent_id(),
                    request.getEpisode_id()
            );
            return ResponseEntity.status(204).body("Episode deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping("/has")
    public ResponseEntity<String> deleteStreamed(@RequestBody DeleteStreamingRequest request) {
        try {
            contentService.deleteFromCurrentlyStreaming(
                    request.getStream_id(),
                    request.getEmail(),
                    request.getContent_id()
            );
            return ResponseEntity.status(204).body("Stream deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/member")
    public ResponseEntity<String> changeEmail(@RequestBody ChangeEmailRequest request) {
        try {
            contentService.changeEmail(
                    request.getNew_email(),
                    request.getOld_email()
            );
            return ResponseEntity.status(200).body("Email changed.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/subscription")
    public ResponseEntity<String> changeSubscription(@RequestBody ChangeSubscriptionRequest request) {
        try {
            contentService.changeSubscription(
                    request.getSubscription_id(),
                    request.getEmail()
            );
            return ResponseEntity.status(200).body("Subscription changed.");
        }
        catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }

    }


}