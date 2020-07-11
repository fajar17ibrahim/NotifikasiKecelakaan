package com.ta.notifikasikecelakaan.ui.policeofficelocation;

import com.ta.notifikasikecelakaan.model.PoliceOffice;

//public class PoliceOfficeLocationPresenter implements PoliceOfficeLocationContract.Presenter, PoliceOfficeLocationContract.Model.OnFinishedListener {
//
//    private PoliceOfficeLocationContract.Model policeOffceLocationModel;
//
//    private PoliceOfficeLocationContract.View policeOfficeLocationView;
//
//    public PoliceOfficeLocationPresenter(PoliceOfficeLocationContract.View policeOfficeLocationView) {
//        this.policeOfficeLocationView = policeOfficeLocationView;
//        this.policeOffceLocationModel = new PoliceOfficeLocationRequest();
//    }
//
//    @Override
//    public void onFinished(PoliceOffice policeOffice) {
//        policeOfficeLocationView.setDataToView(policeOffice);
//        if ( policeOfficeLocationView != null ) {
//            policeOfficeLocationView.hideProgress();
//        }
//    }
//
//    @Override
//    public void onFailure(Throwable t) {
//        policeOfficeLocationView.onResponseFailure(t);
//        if ( policeOfficeLocationView != null ) {
//            policeOfficeLocationView.hideProgress();
//        }
//    }
//
//    @Override
//    public void requestDataFromServer(String idPoliceOffice) {
//        if ( policeOfficeLocationView != null ) {
//            policeOfficeLocationView.hideProgress();
//        }
//        policeOffceLocationModel.getPoliceLocationOffice(this, idPoliceOffice);
//    }
//}
