package com.ta.notifikasikecelakaan.ui.riwayat;

import java.util.ArrayList;

public class RiwayatData {
    public static String[][] data = new String[][]{
            {"Jl", "Jl. Kertajaya Indah", "12.00"},
            {"Eva Putri", "Ir. Soekarno", "13.00"},
            {"Eva Putri", "Ir. Soekarno", "13.00"},
            {"Eva Putri", "Ir. Soekarno", "13.00"},
            {"Satria Putra", "Arif Rahmad Hakim", "14.00"},
            {"Eva Putri", "Ir. Soekarno", "13.00"},
            {"Eva Putri", "Ir. Soekarno", "13.00"},
            {"Eva Putri", "Ir. Soekarno", "13.00"},
            {"Eva Putri", "Ir. Soekarno", "13.00"},
            {"Eva Putri", "Ir. Soekarno", "13.00"},
    };

    public static ArrayList<Riwayat> getListData() {
        ArrayList<Riwayat> list = new ArrayList<>();
        for (String[] aData : data) {
            Riwayat riwayat = new Riwayat();
            riwayat.setKeterangan(aData[0]);
            riwayat.setLokasi(aData[1]);
            riwayat.setWaktu(aData[2]);
            list.add(riwayat);
        }
        return list;
    }
}
