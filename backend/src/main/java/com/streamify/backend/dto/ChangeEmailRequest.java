package com.streamify.backend.dto;

public class ChangeEmailRequest {
    private String old_email;
    private String new_email;

    public ChangeEmailRequest(){

    }

    public ChangeEmailRequest(String old_email, String new_email) {
        this.old_email = old_email;
        this.new_email = new_email;
    }

    public String getOld_email() {return old_email;}
    public void setOld_email(String old_email) {this.old_email = old_email;}
    public String getNew_email() {return new_email;}
    public void setNew_email(String new_email) {this.new_email = new_email;}

}
