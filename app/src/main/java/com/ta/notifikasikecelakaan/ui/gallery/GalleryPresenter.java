package com.ta.notifikasikecelakaan.ui.gallery;

import com.ta.notifikasikecelakaan.model.Gallery;

import java.util.List;

public class GalleryPresenter implements GalleryContract.Presenter, GalleryContract.Model.OnFinishedListener {

    private GalleryContract.View galleryView;

    private GalleryContract.Model galleryModel;

    public GalleryPresenter(GalleryContract.View galleryView) {
        this.galleryView = galleryView;
        galleryModel = new GalleryRequest();
    }

    @Override
    public void onFinished(List<Gallery> galleryList) {
        galleryView.setDataToRecyclerView(galleryList);
        if ( galleryView != null ) {
            galleryView.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        galleryView.onResponseFailure(t);
        if ( galleryView != null ) {
            galleryView.hideProgress();
        }
    }

    @Override
    public void requestDataFromServer(String history_id) {
        if ( galleryView != null ) {
            galleryView.showProgress();
        }
        galleryModel.getGallery(this, history_id);
    }
}
