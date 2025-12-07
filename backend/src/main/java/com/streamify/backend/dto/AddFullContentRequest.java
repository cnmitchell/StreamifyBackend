
package com.streamify.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AddFullContentRequest {
    private String content_name;
    private String release_date;
    @JsonProperty("IMDB_link")
    private String IMDB_link;
    private String genre;
    private String poster_url;
    private String sequel_to;
    private String total_episodes;
    private String total_seasons;
    private List<PersonRequest> cast;
    private List<PersonRequest> directors;
    private List<AwardRequest> awards;
    private List<EpisodeRequest> episodes; // Add episodes list

    public String getContent_name() {
        return content_name;
    }

    public void setContent_name(String content_name) {
        this.content_name = content_name;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getIMDB_link() {
        return IMDB_link;
    }

    public void setIMDB_link(String IMDB_link) {
        this.IMDB_link = IMDB_link;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public String getSequel_to() {
        return sequel_to;
    }

    public void setSequel_to(String sequel_to) {
        this.sequel_to = sequel_to;
    }

    public String getTotal_episodes() {
        return total_episodes;
    }

    public void setTotal_episodes(String total_episodes) {
        this.total_episodes = total_episodes;
    }

    public String getTotal_seasons() {
        return total_seasons;
    }

    public void setTotal_seasons(String total_seasons) {
        this.total_seasons = total_seasons;
    }

    public List<PersonRequest> getCast() {
        return cast;
    }

    public void setCast(List<PersonRequest> cast) {
        this.cast = cast;
    }

    public List<PersonRequest> getDirectors() {
        return directors;
    }

    public void setDirectors(List<PersonRequest> directors) {
        this.directors = directors;
    }

    public List<AwardRequest> getAwards() {
        return awards;
    }

    public void setAwards(List<AwardRequest> awards) {
        this.awards = awards;
    }

    public List<EpisodeRequest> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<EpisodeRequest> episodes) {
        this.episodes = episodes;
    }
}