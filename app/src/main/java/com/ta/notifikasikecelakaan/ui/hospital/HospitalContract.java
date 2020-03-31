package com.ta.notifikasikecelakaan.ui.hospital;

import com.ta.notifikasikecelakaan.model.Hospital;

import java.util.List;

public interface HospitalContract {
    interface Model {
        interface OnFinishedListener {
            void onFinished(List<Hospital> hospitalList);

            void onFailure(Throwable t);
        }

        void getHospitals(OnFinishedListener onFinishedListener);

    }

    interface View {
        void showProgress();
        void hideProgress();
        void setDataToRecyclerView(List<Hospital> hospitalList);
        void onResponseFailure(Throwable throwable);
    }

    interface Presenter{
        void requestDataFromServer();
    }
}
