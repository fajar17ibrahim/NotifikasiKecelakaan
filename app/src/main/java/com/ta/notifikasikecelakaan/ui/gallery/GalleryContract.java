package com.ta.notifikasikecelakaan.ui.gallery;

import com.ta.notifikasikecelakaan.model.Gallery;

import java.util.List;

public interface GalleryContract {

    interface Model {
        interface OnFinishedListener {
            void onFinished(List<Gallery> galleryList);

            void onFailure(Throwable t);
        }

        void getGallery(OnFinishedListener onFinishedListener);

    }

    interface View {
        void showProgress();
        void hideProgress();
        void setDataToRecyclerView(List<Gallery> galleryList);
        void onResponseFailure(Throwable throwable);
    }

    interface Presenter{
        void requestDataFromServer();
    }
}
