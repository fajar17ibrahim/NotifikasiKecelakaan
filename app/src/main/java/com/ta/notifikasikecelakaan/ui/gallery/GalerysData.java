package com.ta.notifikasikecelakaan.ui.gallery;

import java.util.ArrayList;

public class GalerysData {
    public static String[][] data = new String[][]{
            {"Samsuddin", "http://borlandnusantara.com/baru/wp-content/uploads/2018/05/BN_galaxy-mall.jpg","12.00"},
            {"Eva Putri", "https://www.jadwalnonton.com/data/images/movies/2017/grand-city-xxi-surabaya_430x280.jpg", "13.00"},
            {"Satria Putra","https://s3.bukalapak.com/bukalapak-kontenz-production/content_attachments/32018/w-744/shutterstock_1039357051_2.jpg", "14.00"},
    };

    public static ArrayList<Galery> getListData() {
        ArrayList<Galery> list = new ArrayList<>();
        for (String[] aData : data) {
            Galery galery = new Galery();
            galery.setNama(aData[0]);
            galery.setGambar(aData[1]);
            galery.setJam(aData[2]);
            list.add(galery);
        }
        return list;

    }
}