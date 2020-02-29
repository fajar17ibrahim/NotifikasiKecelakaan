package com.ta.notifikasikecelakaan.ui.pesan;

import java.util.ArrayList;

public class PesanData {
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

    public static ArrayList<Pesan> getListData() {
        ArrayList<Pesan> list = new ArrayList<>();
        for (String[] aData : data) {
            Pesan pesan = new Pesan();
            pesan.setJudul(aData[0]);
            pesan.setIsi(aData[1]);
            pesan.setWaktu(aData[2]);
            list.add(pesan);
        }
        return list;
    }
}
