package com.ta.notifikasikecelakaan.ui.policeoffice;

import android.util.Log;

import com.ta.notifikasikecelakaan.model.PoliceOffice;
import com.ta.notifikasikecelakaan.network.ApiClient;
import com.ta.notifikasikecelakaan.network.ApiInterface;
import com.ta.notifikasikecelakaan.utils.ApiUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PoliceOfficeRequest implements PoliceOfficeContract.Model{

    @Override
    public void getPoliceOffice(final OnFinishedListener onFinishedListener) {
        ApiInterface apiService = ApiClient.getClient(ApiUtils.BASE_URL_API).create(ApiInterface.class);

        Call<List<PoliceOffice>> call = apiService.getPoliceOffice();
        call.enqueue(new Callback<List<PoliceOffice>>() {
            @Override
            public void onResponse(Call<List<PoliceOffice>> call, Response<List<PoliceOffice>> response) {
                List PoliceOffice = response.body();
                Log.d("Result ", PoliceOffice.toString());
                onFinishedListener.onFinished(PoliceOffice);
            }

            @Override
            public void onFailure(Call<List<PoliceOffice>> call, Throwable t) {
                Log.d("Error Result ", t.toString());
                onFinishedListener.onFailure(t);
            }
        });
    }
}
