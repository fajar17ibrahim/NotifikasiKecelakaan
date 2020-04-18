package com.ta.notifikasikecelakaan.ui.hostipallocation;

import com.ta.notifikasikecelakaan.model.Hospital;

public interface HospitalLocationContract {

    interface Model {

        interface OnFinishedListener {

            void onFinished(Hospital hospital);

            void onFailure(Throwable t);
        }

        void getHospital(OnFinishedListener onFinishedListener, String idHospital);

    }

    interface View {

        void showProgress();

        void hideProgress();

        void setDataToView(Hospital hospital);

        void onResponseFailure(Throwable throwable);
    }

    interface Presenter{
        void requestDataFromServer(String idHospital);
    }
}
