package com.streamify.backend.dto;

public class AddMemberRequest {
    private String email;
    private String password;
    private String name;
    private String street;
    private String city;
    private String state;
    private String country;
    private String phone;
    private String subName;

    public AddMemberRequest(){

    }

    public AddMemberRequest(String email, String password, String name,
                            String street, String city, String state, String country,
                            String phone, String subName){
        this.email = email;
        this.password = password;
        this.name = name;
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.phone = phone;
        this.subName = subName;
    }

    public String getEmail() {return this.email;}
    public void setEmail(String email) {this.email = email;}
    public String getPassword() {return this.password;}
    public void setPassword(String password) {this.password = password;}
    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}
    public String getStreet() {return this.street;}
    public void setStreet(String street) {this.street = street;}
    public String getCity() {return this.city;}
    public void setCity(String city) {this.city = city;}
    public String getState() {return this.state;}
    public void setState(String state) {this.state = state;}
    public String getCountry() {return this.country;}
    public void setCountry(String country) {this.country = country;}
    public String getPhone() {return this.phone;}
    public void setPhone(String phone) {this.phone = phone;}
    public String getSubName() {return this.subName;}
    public void setSubName(String subName) {this.subName = subName;}
}