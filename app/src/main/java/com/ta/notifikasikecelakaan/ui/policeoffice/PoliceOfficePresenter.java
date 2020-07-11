package com.ta.notifikasikecelakaan.ui.policeoffice;

import com.ta.notifikasikecelakaan.model.PoliceOffice;

import java.util.List;

public class PoliceOfficePresenter implements PoliceOfficeContract.Presenter, PoliceOfficeContract.Model.OnFinishedListener {

    private PoliceOfficeContract.View policeOfficeView;

    private PoliceOfficeContract.Model policeOfficeModel;

    public PoliceOfficePresenter(PoliceOfficeContract.View policeOfficeView) {
        this.policeOfficeView = policeOfficeView;
        policeOfficeModel = new PoliceOfficeRequest();
    }

    @Override
    public void requestDataFromServer(String history_id) {
        if ( policeOfficeView != null ) {
            policeOfficeView.showProgress();
        }
        policeOfficeModel.getPoliceOffice(this, history_id);
    }

    @Override
    public void onFinished(List<PoliceOffice> policeOfficeList) {
        policeOfficeView.setDataToRecyclerView(policeOfficeList);
        if ( policeOfficeView != null ) {
            policeOfficeView.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        policeOfficeView.onResponseFailure(t);
        if ( policeOfficeView != null ) {
            policeOfficeView.hideProgress();
        }
    }
}
