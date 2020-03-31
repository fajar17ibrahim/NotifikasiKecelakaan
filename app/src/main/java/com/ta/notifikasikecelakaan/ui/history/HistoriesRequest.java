package com.ta.notifikasikecelakaan.ui.history;

import android.util.Log;

import com.ta.notifikasikecelakaan.model.History;
import com.ta.notifikasikecelakaan.network.ApiClient;
import com.ta.notifikasikecelakaan.network.ApiInterface;
import com.ta.notifikasikecelakaan.utils.ApiUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoriesRequest implements HistoriesContract.Model {
    @Override
    public void getHistories(final OnFinishedListener onFinishedListener) {
        ApiInterface apiInterface = ApiClient.getClient(ApiUtils.BASE_URL_API).create(ApiInterface.class);

        Call<List<History>> call = apiInterface.getHistories();
        call.enqueue(new Callback<List<History>>() {
            @Override
            public void onResponse(Call<List<History>> call, Response<List<History>> response) {
                List histories = response.body();
                Log.d("Result ", histories.toString());
                onFinishedListener.onFinished(histories);
            }

            @Override
            public void onFailure(Call<List<History>> call, Throwable t) {
                Log.d("Result Error ", t.toString());
                onFinishedListener.onFailure(t);
            }
        });
    }
}
