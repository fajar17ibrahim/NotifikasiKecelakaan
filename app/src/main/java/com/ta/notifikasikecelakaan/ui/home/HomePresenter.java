package com.ta.notifikasikecelakaan.ui.home;

import com.ta.notifikasikecelakaan.model.Accident;

public class HomePresenter implements HomeContract.Presenter, HomeContract.Model.OnFinishedListener {

    private HomeContract.Model homeModel;

    private HomeContract.View homeView;

    public HomePresenter(HomeContract.View homeView) {
        this.homeView = homeView;
        this.homeModel = new HomeRequest();
    }

    @Override
    public void onFinished(Accident accident) {
        homeView.setDataToView(accident);
        if ( homeView != null ) {
            homeView.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        homeView.onResponseFailure(t);
        if ( homeView != null ) {
            homeView.hideProgress();
        }
    }

    @Override
    public void requestDataFromServer(int user_id) {
        if ( homeView != null ) {
            homeView.showProgress();
        }
        homeModel.getAccident(this, user_id);
    }
}
