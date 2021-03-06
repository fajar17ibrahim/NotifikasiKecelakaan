package com.ta.notifikasikecelakaan.model;

import com.google.gson.annotations.SerializedName;

public class PoliceOffice {

    @SerializedName("policeoffice_id")
    private String policeoffice_id;

    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("phone")
    private String phone;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    public String getPoliceoffice_id() {
        return policeoffice_id;
    }

    public void setPoliceoffice_id(String policeoffice_id) {
        this.policeoffice_id = policeoffice_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public PoliceOffice(String policeoffice_id, String name, String address, String phone, double latitude, double longitude) {
        this.policeoffice_id = policeoffice_id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
