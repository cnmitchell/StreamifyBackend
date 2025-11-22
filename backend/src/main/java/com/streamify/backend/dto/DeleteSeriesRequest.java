package com.streamify.backend.dto;

public class DeleteSeriesRequest {
    private String content_id;

    public DeleteSeriesRequest() {

    }

    public DeleteSeriesRequest(String content_id) {
        this.content_id = content_id;
    }

    public String getContent_id() {return this.content_id;}
    public void setContent_id(String content_id) {this.content_id = content_id;}

}
