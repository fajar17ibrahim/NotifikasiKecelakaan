package com.ta.notifikasikecelakaan.model;

import com.google.gson.annotations.SerializedName;

public class Gallery {

    @SerializedName("gallery_id")
    private int gallery_id;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("image")
    private String image;

    @SerializedName("name")
    private String name;

    public int getGallery_id() {
        return gallery_id;
    }

    public void setGallery_id(int gallery_id) {
        this.gallery_id = gallery_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gallery(int gallery_id, String created_at, String image, String name) {
        this.gallery_id = gallery_id;
        this.created_at = created_at;
        this.image = image;
        this.name = name;
    }
}
