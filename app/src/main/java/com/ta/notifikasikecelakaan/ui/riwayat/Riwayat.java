package com.ta.notifikasikecelakaan.ui.riwayat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Riwayat {
    private String keterangan;
    private String lokasi;
    private String waktu;

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
