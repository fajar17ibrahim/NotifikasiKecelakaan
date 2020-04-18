package com.ta.notifikasikecelakaan.ui.policeofficelocation;

import com.ta.notifikasikecelakaan.model.PoliceOffice;

public interface PoliceOfficeLocationContract {

    interface Model {

        interface OnFinishedListener {

            void onFinished(PoliceOffice policeOffice);

            void onFailure(Throwable t);

        }

        void getPoliceLocationOffice(OnFinishedListener onFinishedListener, String idPoliceOffice);

    }

    interface View {

        void showProgress();

        void hideProgress();

        void setDataToView(PoliceOffice policeOffice);

        void onResponseFailure(Throwable t);

    }

    interface Presenter {

        void requestDataFromServer(String idPoliceOffice);
    }

}
