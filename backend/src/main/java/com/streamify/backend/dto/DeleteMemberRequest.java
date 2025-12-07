package com.streamify.backend.dto;

public class DeleteMemberRequest {
    private String email;
    private String member_id;

    public DeleteMemberRequest() {}

    public DeleteMemberRequest(String email, String member_id) {
        this.email = email;
        this.member_id = member_id;
    }

    public String getEmail() {return this.email;}
    public void setEmail(String email) {this.email = email;}
    public String getMember_id() {return this.member_id;}
    public void setMember_id(String member_id) {this.member_id = member_id;}
}