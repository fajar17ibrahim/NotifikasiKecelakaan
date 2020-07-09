package com.ta.notifikasikecelakaan.ui.gallery;


import android.util.Log;

import com.ta.notifikasikecelakaan.model.Gallery;
import com.ta.notifikasikecelakaan.network.ApiClient;
import com.ta.notifikasikecelakaan.network.ApiInterface;
import com.ta.notifikasikecelakaan.utils.ApiUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryRequest implements GalleryContract.Model {
    @Override
    public void getGallery(final OnFinishedListener onFinishedListener, String historyId) {
        ApiInterface apiInterface = ApiClient.getClient(ApiUtils.BASE_URL_API).create(ApiInterface.class);

        Call<List<Gallery>> call = apiInterface.getGallery(historyId);
        call.enqueue(new Callback<List<Gallery>>() {
            @Override
            public void onResponse(Call<List<Gallery>> call, Response<List<Gallery>> response) {
                List gallery = response.body();
                Log.d("Result ", gallery.toString());
                onFinishedListener.onFinished(gallery);
            }

            @Override
            public void onFailure(Call<List<Gallery>> call, Throwable t) {
                Log.d("Result Error ", t.toString());
                onFinishedListener.onFailure(t);
            }
        });
    }
}
