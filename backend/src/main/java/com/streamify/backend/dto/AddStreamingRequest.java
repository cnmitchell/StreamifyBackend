package com.streamify.backend.dto;

public class AddStreamingRequest {
    private String email;
    private String content_id;
    private String episode_id;

    public AddStreamingRequest() {}

    public AddStreamingRequest(String email, String content_id) {
        this.email = email;
        this.content_id = content_id;
        this.episode_id = episode_id;
    }

    public String getEmail() {return this.email;}
    public void setEmail(String email) {this.email = email;}
    public String getContent_id() {return this.content_id;}
    public void setContent_id(String content_id) {this.content_id = content_id;}
    public String getEpisode_id() {return this.episode_id;}
    public void setEpisode_id(String episode_id) {this.episode_id = episode_id;}
}