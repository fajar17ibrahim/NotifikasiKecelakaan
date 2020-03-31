package com.ta.notifikasikecelakaan.model;

import com.google.gson.annotations.SerializedName;

public class Respondent {

    @SerializedName("respondent_id")
    private int respondent_id;

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    @SerializedName("password")
    private String password;

    public int getRespondent_id() {
        return respondent_id;
    }

    public void setRespondent_id(int respondent_id) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Respondent(int respondent_id, String name, String phone, String password) {
        this.respondent_id = respondent_id;
        this.name = name;
        this.phone = phone;
        this.password = password;
    }
}
