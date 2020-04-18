package com.ta.notifikasikecelakaan.ui.setting.editprofile;

import com.ta.notifikasikecelakaan.model.Respondent;

public class ProfilePresenter implements ProfileContract.Presenter, ProfileContract.Model.OnFinishedListener {

    private ProfileContract.Model profileModel;

    private ProfileContract.View profileView;

    public ProfilePresenter(ProfileContract.View profileView) {
        this.profileView = profileView;
        profileModel = new ProfileRequest();
    }

    @Override
    public void onFinshed(Respondent respondent){
        profileView.setDataToView(respondent);
        if ( profileView != null ) {
            profileView.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable e) {
        profileView.onResponseFailure(e);
        if ( profileView != null ) {
            profileView.hideProgress();
        }
    }

    @Override
    public void requestDataFromServer(String id) {
        if ( profileView != null ) {
            profileView.showProgress();
        }
        profileModel.getRespondent(this, id);
    }
}
