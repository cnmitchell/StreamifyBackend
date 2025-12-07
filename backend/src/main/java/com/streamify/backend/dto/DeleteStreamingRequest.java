package com.streamify.backend.dto;

public class DeleteStreamingRequest {
    private String email;
    private String content_id;

    public DeleteStreamingRequest() {}

    public DeleteStreamingRequest(String stream_id, String email, String content_id) {
        this.email = email;
        this.content_id = content_id;
    }
    public String getEmail() {return this.email;}
    public void setEmail(String email) {this.email = email;}
    public String getContent_id() {return this.content_id;}
    public void setContent_id(String content_id) {this.content_id = content_id;}
}