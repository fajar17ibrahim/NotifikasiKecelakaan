package com.ta.notifikasikecelakaan.ui.gallerydetails;

import android.util.Log;

import com.ta.notifikasikecelakaan.model.Gallery;
import com.ta.notifikasikecelakaan.network.ApiClient;
import com.ta.notifikasikecelakaan.network.ApiInterface;
import com.ta.notifikasikecelakaan.utils.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryDetailsRequest implements GalleryDetailsContract.Model {
    @Override
    public void getGallery(final OnFinishedListener onFinishedListener, String galleryId) {
        ApiInterface apiInterface = ApiClient.getClient(ApiUtils.BASE_URL_API).create(ApiInterface.class);

        Call<Gallery> call = apiInterface.getGalleryDetail(galleryId);
        call.enqueue(new Callback<Gallery>() {
            @Override
            public void onResponse(Call<Gallery> call, Response<Gallery> response) {
                Gallery gallery = response.body();
                Log.d("Result ", gallery.toString());
                onFinishedListener.onFinished(gallery);
            }

            @Override
            public void onFailure(Call<Gallery> call, Throwable t) {
                Log.d("Error Result ", t.toString());
                onFinishedListener.onFailure(t);
            }
        });

    }
}
