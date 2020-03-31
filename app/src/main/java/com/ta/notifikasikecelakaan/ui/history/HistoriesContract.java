package com.ta.notifikasikecelakaan.ui.history;

import com.ta.notifikasikecelakaan.model.History;

import java.util.List;

public interface HistoriesContract {
    interface Model {
        interface OnFinishedListener {
            void onFinished(List<History> historyList);

            void onFailure(Throwable t);
        }

        void getHistories(OnFinishedListener onFinishedListener);

    }

    interface View {
        void showProgress();
        void hideProgress();
        void setDataToRecyclerView(List<History> historyList);
        void onResponseFailure(Throwable throwable);
    }

    interface Presenter{
        void requestDataFromServer();
    }
}
