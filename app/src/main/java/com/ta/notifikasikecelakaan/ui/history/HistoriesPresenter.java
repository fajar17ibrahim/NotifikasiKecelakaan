package com.ta.notifikasikecelakaan.ui.history;

import com.ta.notifikasikecelakaan.model.History;

import java.util.List;

public class HistoriesPresenter implements HistoriesContract.Presenter, HistoriesContract.Model.OnFinishedListener {

    private HistoriesContract.View historyView;

    private HistoriesContract.Model historyModel;

    public HistoriesPresenter(HistoriesContract.View historyView) {
        this.historyView = historyView;
        historyModel = new HistoriesRequest();
    }

    @Override
    public void onFinished(List<History> historyList) {
        historyView.setDataToRecyclerView(historyList);
        if ( historyView != null ) {
            historyView.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        historyView.onResponseFailure(t);
        if ( historyView != null ) {
            historyView.hideProgress();
        }
    }

    @Override
    public void requestDataFromServer() {
        if ( historyView != null ) {
            historyView.showProgress();
        }
        historyModel.getHistories(this);
    }
}
