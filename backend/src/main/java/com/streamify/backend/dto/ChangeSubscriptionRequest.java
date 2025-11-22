package com.streamify.backend.dto;

public class ChangeSubscriptionRequest {
    private String subscription_id;
    private String email;

    public ChangeSubscriptionRequest() {

    }

    public ChangeSubscriptionRequest(String subscription_id, String email) {
        this.subscription_id = subscription_id;
        this.email = email;
    }

    public String getSubscription_id() {return subscription_id;}
    public void setSubscription_id(String subscription_id) {this.subscription_id = subscription_id;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

}
