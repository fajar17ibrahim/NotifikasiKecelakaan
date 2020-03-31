package com.ta.notifikasikecelakaan.ui.policeoffice;

import com.ta.notifikasikecelakaan.model.PoliceOffice;

import java.util.List;

public interface PoliceOfficeContract {
    interface Model {
        interface OnFinishedListener {
            void onFinished(List<PoliceOffice> policeOfficeList);

            void onFailure(Throwable t);
        }

        void getPoliceOffice(OnFinishedListener onFinishedListener);

    }

    interface View {
        void showProgress();
        void hideProgress();
        void setDataToRecyclerView(List<PoliceOffice> policeOfficeList);
        void onResponseFailure(Throwable throwable);
    }

    interface Presenter{
        void requestDataFromServer();
    }
}
