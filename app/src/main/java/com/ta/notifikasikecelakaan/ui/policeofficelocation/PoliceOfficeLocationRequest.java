package com.ta.notifikasikecelakaan.ui.policeofficelocation;

import android.util.Log;

import com.ta.notifikasikecelakaan.model.PoliceOffice;
import com.ta.notifikasikecelakaan.network.ApiClient;
import com.ta.notifikasikecelakaan.network.ApiInterface;
import com.ta.notifikasikecelakaan.utils.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PoliceOfficeLocationRequest implements PoliceOfficeLocationContract.Model {

    @Override
    public void getPoliceLocationOffice(final OnFinishedListener onFinishedListener, String idPoliceOffice) {
        ApiInterface apiClient = ApiClient.getClient(ApiUtils.BASE_URL_API).create(ApiInterface.class);

        Call<PoliceOffice> call = apiClient.getPoliceOfficeLocation(idPoliceOffice);
        call.enqueue(new Callback<PoliceOffice>() {
            @Override
            public void onResponse(Call<PoliceOffice> call, Response<PoliceOffice> response) {
                PoliceOffice policeOffice = response.body();
                Log.d("Result ", policeOffice.toString());
                onFinishedListener.onFinished(policeOffice);
            }

            @Override
            public void onFailure(Call<PoliceOffice> call, Throwable t) {
                Log.d("Error Result ", t.toString());
                onFinishedListener.onFailure(t);
            }
        });
    }
}
