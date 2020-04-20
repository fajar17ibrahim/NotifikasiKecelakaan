package com.ta.notifikasikecelakaan.ui.home;

import com.ta.notifikasikecelakaan.model.Accident;

public interface HomeContract {

    interface Model {

        interface OnFinishedListener {

            void onFinished(Accident accident);

            void onFailure(Throwable t);
        }

        void getAccident(OnFinishedListener onFinishedListener);

    }

    interface View {

        void showProgress();

        void hideProgress();

        void setDataToView(Accident accident);

        void onResponseFailure(Throwable throwable);
    }

    interface Presenter{

        void requestDataFromServer();

    }


}
