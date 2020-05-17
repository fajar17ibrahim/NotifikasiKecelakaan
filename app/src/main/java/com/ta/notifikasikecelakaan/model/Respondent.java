package com.ta.notifikasikecelakaan.model;

import com.google.gson.annotations.SerializedName;

public class Respondent {

    @SerializedName("respondent_id")
    private String respondent_id;

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    @SerializedName("password")
    private String password;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Respondent(String respondent_id, String name, String phone, String password, double latitude, double longitude) {
        this.respondent_id = respondent_id;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
