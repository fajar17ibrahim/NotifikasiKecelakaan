package com.ta.notifikasikecelakaan.ui.hostipallocation;

import com.ta.notifikasikecelakaan.model.Hospital;

//public class HospitalLocationPresenter implements HospitalLocationContract.Presenter, HospitalLocationContract.Model.OnFinishedListener {
//
//    private HospitalLocationContract.Model hospitalLocationModel;
//
//    private HospitalLocationContract.View hospitalLocationView;
//
//
//    public HospitalLocationPresenter(HospitalLocationContract.View hospitalLocationView) {
//        this.hospitalLocationView = hospitalLocationView;
//        this.hospitalLocationModel = new HospitalLocationRequest();
//    }
//
//    @Override
//    public void onFinished(Hospital hospital) {
//        hospitalLocationView.setDataToView(hospital);
//        if ( hospitalLocationView != null ) {
//            hospitalLocationView.hideProgress();
//        }
//    }
//
//    @Override
//    public void onFailure(Throwable t) {
//        hospitalLocationView.onResponseFailure(t);
//        if ( hospitalLocationView != null ) {
//            hospitalLocationView.hideProgress();
//        }
//    }
//
//    @Override
//    public void requestDataFromServer(String idHospital) {
//        if ( hospitalLocationView != null ) {
//            hospitalLocationView.showProgress();
//        }
//        hospitalLocationModel.getHospital(this, idHospital);
//    }
//}
