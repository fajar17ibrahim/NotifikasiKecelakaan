package com.ta.notifikasikecelakaan.ui.rs;

import java.util.ArrayList;

public class RsData {
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

    public static ArrayList<Rs> getListData() {
        ArrayList<Rs> list = new ArrayList<>();
        for (String[] aData : data) {
            Rs rs = new Rs();
            rs.setNama(aData[0]);
            rs.setAlamat(aData[1]);
            rs.setTelp(aData[2]);
            list.add(rs);
        }
        return list;
    }
}
