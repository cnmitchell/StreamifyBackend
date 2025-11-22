package com.streamify.backend.dto;

public class AddEpisodeRequest {
    private String content_id;
    private String episode_id;
    private String season_number;
    private String episode_number;
    private String title;
    private String release_date;

    public AddEpisodeRequest() {

    }

    public AddEpisodeRequest(String content_id, String episode_id, String season_number, String episode_number, String title, String release_date) {
        this.content_id = content_id;
        this.episode_id = episode_id;
        this.season_number = season_number;
        this.episode_number = episode_number;
        this.title = title;
        this.release_date = release_date;
    }

    public String getContent_id() {return content_id;}
    public void setContent_id(String content_id) {this.content_id = content_id;}
    public String getEpisode_id() {return episode_id;}
    public void setEpisode_id(String episode_id) {this.episode_id = episode_id;}
    public String getSeason_number() {return season_number;}
    public void setSeason_number(String season_number) {this.season_number = season_number;}
    public String getEpisode_number() {return episode_number;}
    public void setEpisode_number(String episode_number) {this.episode_number = episode_number;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getRelease_date() {return release_date;}
    public void setRelease_date(String release_date) {this.release_date = release_date;}


}
