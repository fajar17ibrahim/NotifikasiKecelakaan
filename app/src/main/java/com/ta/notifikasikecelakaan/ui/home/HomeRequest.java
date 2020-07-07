package com.ta.notifikasikecelakaan.ui.home;

import android.util.Log;

import com.ta.notifikasikecelakaan.model.Accident;
import com.ta.notifikasikecelakaan.network.ApiClient;
import com.ta.notifikasikecelakaan.network.ApiInterface;
import com.ta.notifikasikecelakaan.utils.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRequest implements HomeContract.Model {

    @Override
    public void getAccident(OnFinishedListener onFinishedListener, int user_id) {
        ApiInterface apiInterface = ApiClient.getClient(ApiUtils.BASE_URL_API).create(ApiInterface.class);

        Call<Accident> call = apiInterface.getAccident(user_id);
        call.enqueue(new Callback<Accident>() {
            @Override
            public void onResponse(Call<Accident> call, Response<Accident> response) {
                Accident accident = response.body();
                Log.d("Result ", accident.toString());
                onFinishedListener.onFinished(accident);
            }

            @Override
            public void onFailure(Call<Accident> call, Throwable t) {
                Log.d("Result Error ", t.toString());
                onFinishedListener.onFailure(t);
            }
        });
    }
}
