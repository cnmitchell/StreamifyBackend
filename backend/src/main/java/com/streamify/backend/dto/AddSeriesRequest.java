package com.streamify.backend.dto;

public class AddSeriesRequest {
    private String content_name;
    private String release_date;
    private String IMDB_link;
    private String genre ;
    private String poster_url;
    private String total_episodes;
    private String total_seasons;

    AddSeriesRequest() {

    }

    public AddSeriesRequest(String content_name, String release_date,
                           String IMDB_link, String genre, String poster_url,
                            String total_episodes, String total_seasons) {
        this.content_name = content_name;
        this.release_date = release_date;
        this.IMDB_link = IMDB_link;
        this.genre = genre;
        this.poster_url = poster_url;
        this.total_episodes = total_episodes;
        this.total_seasons = total_seasons;
    }

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
}
