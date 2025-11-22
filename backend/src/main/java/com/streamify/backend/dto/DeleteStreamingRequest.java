package com.streamify.backend.dto;

public class DeleteStreamingRequest {
    private String stream_id;
    private String email;
    private String content_id;

    public DeleteStreamingRequest() {

    }

    public DeleteStreamingRequest(String stream_id, String email, String content_id) {
        this.stream_id = stream_id;
        this.email = email;
        this.content_id = content_id;
    }
    public String getStream_id() {return this.stream_id;}
    public void setStream_id(String stream_id) {this.stream_id = stream_id;}
    public String getEmail() {return this.email;}
    public void setEmail(String email) {this.email = email;}
    public String getContent_id() {return this.content_id;}
    public void setContent_id(String content_id) {this.content_id = content_id;}
}
