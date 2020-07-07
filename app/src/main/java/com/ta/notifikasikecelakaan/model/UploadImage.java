package com.ta.notifikasikecelakaan.model;

import com.google.gson.annotations.SerializedName;

public class UploadImage {

    @SerializedName("code")
    private String code;

    @SerializedName("message")
    private String message;

    @SerializedName("respondent_id")
    private String respondent_id;

    @SerializedName("history_id")
    private String history_id;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRespondent_id() {
        return respondent_id;
    }

    public void setRespondent_id(String respondent_id) {
        this.respondent_id = respondent_id;
    }

    public String getHistory_id() {
        return history_id;
    }

    public void setHistory_id(String history_id) {
        this.history_id = history_id;
    }

    public UploadImage(String code, String message, String respondent_id, String history_id) {
        this.code = code;
        this.message = message;
        this.respondent_id = respondent_id;
        this.history_id = history_id;
    }
}
