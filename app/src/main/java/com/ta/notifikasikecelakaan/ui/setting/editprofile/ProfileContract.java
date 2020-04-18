package com.ta.notifikasikecelakaan.ui.setting.editprofile;

import com.ta.notifikasikecelakaan.model.Respondent;

public interface ProfileContract {

    interface Model {

        interface OnFinishedListener {

            void onFinshed(Respondent respondent);

            void onFailure(Throwable e);
        }

        void getRespondent(OnFinishedListener onFinishedListener, String id);

    }

    interface View {

        void showProgress();

        void hideProgress();

        void setDataToView(Respondent respondent);

        void onResponseFailure (Throwable throwable);
    }

    interface Presenter {

        void requestDataFromServer(String id);

    }
}
