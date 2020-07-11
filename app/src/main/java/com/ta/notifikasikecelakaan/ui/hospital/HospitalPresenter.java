package com.ta.notifikasikecelakaan.ui.hospital;

import com.ta.notifikasikecelakaan.model.Hospital;

import java.util.List;

public class HospitalPresenter implements HospitalContract.Presenter, HospitalContract.Model.OnFinishedListener {

    private HospitalContract.View hospitalView;

    private HospitalContract.Model hospitalModel;

    public HospitalPresenter(HospitalContract.View hospitalView) {
        this.hospitalView = hospitalView;
        hospitalModel = new HospitalRequest();
    }

    @Override
    public void onFinished(List<Hospital> hospitalList) {
        hospitalView.setDataToRecyclerView(hospitalList);
        if ( hospitalView != null ) {
            hospitalView.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        hospitalView.onResponseFailure(t);
        if ( hospitalView != null ) {
            hospitalView.hideProgress();
        }
    }

    @Override
    public void requestDataFromServer(String history_id) {
        if ( hospitalView != null ) {
            hospitalView.showProgress();
        }
        hospitalModel.getHospitals(this, history_id);
    }
}
