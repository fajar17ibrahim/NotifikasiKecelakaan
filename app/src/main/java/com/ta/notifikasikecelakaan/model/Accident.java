package com.ta.notifikasikecelakaan.model;

import com.google.gson.annotations.SerializedName;

public class Accident {

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("history_id")
    private String history_id;

    @SerializedName("time")
    private String time;

    @SerializedName("address")
    private String address;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("status")
    private String status;

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    @SerializedName("family_phone")
    private String family_phone;

    @SerializedName("password")
    private String password;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getHistory_id() {
        return history_id;
    }

    public void setHistory_id(String history_id) {
        this.history_id = history_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getFamily_phone() {
        return family_phone;
    }

    public void setFamily_phone(String family_phone) {
        this.family_phone = family_phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Accident(String user_id, String history_id, String time, String address, Double latitude, Double longitude, String status, String name, String phone, String family_phone, String password) {
        this.user_id = user_id;
        this.history_id = history_id;
        this.time = time;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.name = name;
        this.phone = phone;
        this.family_phone = family_phone;
        this.password = password;
    }
}
