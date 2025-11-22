package com.streamify.backend.dto;

public class DeleteEpisodeRequest {
    private String content_id;
    private String episode_id;

    public DeleteEpisodeRequest() {

    }

    public DeleteEpisodeRequest(String content_id, String episode_id) {
        this.content_id = content_id;
        this.episode_id = episode_id;
    }

    public String getContent_id() {return this.content_id;}
    public void setContent_id(String content_id) {this.content_id = content_id;}
    public String getEpisode_id() {return this.episode_id;}
    public void setEpisode_id(String  episode_id) {this.episode_id = episode_id;}
}
