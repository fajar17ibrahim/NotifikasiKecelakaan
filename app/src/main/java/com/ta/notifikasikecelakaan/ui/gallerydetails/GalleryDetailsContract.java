package com.ta.notifikasikecelakaan.ui.gallery_details;

import com.ta.notifikasikecelakaan.model.Gallery;

public interface GalleryDetailsContract {
    interface Model {
        interface OnFinishedListener {
            void onFinished(Gallery gallery);

            void onFailure(Throwable t);
        }

        void getGallery(OnFinishedListener onFinishedListener, int galleryId);

    }

    interface View {
        void showProgress();
        void hideProgress();
        void setDataToView(Gallery gallery);
        void onResponseFailure(Throwable throwable);
    }

    interface Presenter{
        void requestDataFromServer(int galleryId);
    }
}
