package com.ta.notifikasikecelakaan.ui.gallery_details;

import com.ta.notifikasikecelakaan.model.Gallery;

public class GalleryDetailsPresenter implements GalleryDetailsContract.Presenter, GalleryDetailsContract.Model.OnFinishedListener {

    private GalleryDetailsContract.View galleryDetailsView;

    private GalleryDetailsContract.Model galleryDetailsModel;

    public GalleryDetailsPresenter(GalleryDetailsContract.View galleryDetailsView) {
        this.galleryDetailsView = galleryDetailsView;
        galleryDetailsModel = new GalleryDetailsRequest();
    }

    @Override
    public void onFinished(Gallery gallery) {
        galleryDetailsView.setDataToView(gallery);
        if ( galleryDetailsView != null ) {
            galleryDetailsView.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        galleryDetailsView.onResponseFailure(t);
        if ( galleryDetailsView != null ) {
            galleryDetailsView.hideProgress();
        }
    }

    @Override
    public void requestDataFromServer(int galleryId) {
        if ( galleryDetailsView != null ) {
            galleryDetailsView.showProgress();
        }
        galleryDetailsModel.getGallery(this, galleryId);
    }
}
