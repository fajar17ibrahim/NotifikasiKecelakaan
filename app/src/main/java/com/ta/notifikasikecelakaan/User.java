package com.ta.notifikasikecelakaan;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    String id, nama, telp_user, telp_kel, pass;


    protected User(Parcel in) {
        id = in.readString();
        nama = in.readString();
        telp_user = in.readString();
        telp_kel = in.readString();
        pass = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nama);
        dest.writeString(telp_user);
        dest.writeString(telp_kel);
        dest.writeString(pass);
    }

    public User() {
    }
}
