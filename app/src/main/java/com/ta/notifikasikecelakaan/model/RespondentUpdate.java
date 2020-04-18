package com.ta.notifikasikecelakaan.model;

import com.google.gson.annotations.SerializedName;

public class RespondentUpdate {

    @SerializedName("respondent_id")
    private String respondent_id;

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    public String getRespondent_id() {
        return respondent_id;
    }

    public void setRespondent_id(String respondent_id) {
        this.respondent_id = respondent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public RespondentUpdate() {
        this.respondent_id = respondent_id;
        this.name = name;
        this.phone = phone;
    }
}
