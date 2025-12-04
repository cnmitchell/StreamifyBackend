package com.streamify.backend.dto;

public class AddEpisodeRequest {
    private String content_id;
    private int season_number;
    private int episode_number;
    private String title;

    public AddEpisodeRequest() {

    }

    public AddEpisodeRequest(String content_id, int season_number, int episode_number, String title, String release_date) {
        this.content_id = content_id;
        this.season_number = season_number;
        this.episode_number = episode_number;
        this.title = title;
    }

    public String getContent_id() {return content_id;}
    public void setContent_id(String content_id) {this.content_id = content_id;}
    public int getSeason_number() {return season_number;}
    public void setSeason_number(int season_number) {this.season_number = season_number;}
    public int getEpisode_number() {return episode_number;}
    public void setEpisode_number(int episode_number) {this.episode_number = episode_number;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
}
