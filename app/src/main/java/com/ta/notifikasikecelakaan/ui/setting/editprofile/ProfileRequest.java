package com.ta.notifikasikecelakaan.ui.setting.editprofile;

import android.util.Log;

import com.ta.notifikasikecelakaan.model.Respondent;
import com.ta.notifikasikecelakaan.network.ApiClient;
import com.ta.notifikasikecelakaan.network.ApiInterface;
import com.ta.notifikasikecelakaan.utils.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRequest implements ProfileContract.Model {
    @Override
    public void getRespondent(final OnFinishedListener onFinishedListener, String id) {
        ApiInterface apiService = ApiClient.getClient(ApiUtils.BASE_URL_API).create(ApiInterface.class);

        Call<Respondent> call = apiService.getRespondentDetail(id);
        call.enqueue(new Callback<Respondent>() {
            @Override
            public void onResponse(Call<Respondent> call, Response<Respondent> response) {
                Respondent respondent = response.body();
                Log.v("Result ", respondent.toString());
                onFinishedListener.onFinshed(respondent);
            }

            @Override
            public void onFailure(Call<Respondent> call, Throwable t) {
                Log.v("Error Result ", t.toString());
                onFinishedListener.onFailure(t);
            }
        });

    }
}
