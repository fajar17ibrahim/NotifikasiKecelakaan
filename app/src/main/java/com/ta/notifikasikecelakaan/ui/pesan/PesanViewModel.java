package com.ta.notifikasikecelakaan.ui.pesan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PesanViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PesanViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is tools fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}