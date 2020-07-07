package com.ta.notifikasikecelakaan.model;

import com.google.gson.annotations.SerializedName;

public class Accident {

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    @SerializedName("family_phone")
    private String family_phone;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("kondisi")
    private String kondisi;

    @SerializedName("triger")
    private String triger;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }

    public String getTriger() {
        return triger;
    }

    public void setTriger(String triger) {
        this.triger = triger;
    }

    public Accident(String user_id, String name, String phone, String family_phone, double latitude, double longitude, String kondisi, String triger) {
        this.user_id = user_id;
        this.name = name;
        this.phone = phone;
        this.family_phone = family_phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.kondisi = kondisi;
        this.triger = triger;
    }
}
