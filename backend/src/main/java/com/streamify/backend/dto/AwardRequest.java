
package com.streamify.backend.dto;

public class AwardRequest {
    private String award_name;
    private String award_year;

    public String getAward_name() {
        return award_name;
    }

    public void setAward_name(String award_name) {
        this.award_name = award_name;
    }

    public String getAward_year() {
        return award_year;
    }

    public void setAward_year(String award_year) {
        this.award_year = award_year;
    }
}
