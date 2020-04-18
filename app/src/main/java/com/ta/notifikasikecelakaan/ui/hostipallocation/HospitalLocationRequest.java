package com.ta.notifikasikecelakaan.ui.hostipallocation;

import android.util.Log;

import com.ta.notifikasikecelakaan.model.Hospital;
import com.ta.notifikasikecelakaan.network.ApiClient;
import com.ta.notifikasikecelakaan.network.ApiInterface;
import com.ta.notifikasikecelakaan.ui.hospital.HospitalContract;
import com.ta.notifikasikecelakaan.utils.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalLocationRequest implements HospitalLocationContract.Model {

    @Override
    public void getHospital(final OnFinishedListener onFinishedListener, String idHospital) {
        ApiInterface apiInterface = ApiClient.getClient(ApiUtils.BASE_URL_API).create(ApiInterface.class);

        Call<Hospital> call = apiInterface.getHospitalLocation(idHospital);
        call.enqueue(new Callback<Hospital>() {
            @Override
            public void onResponse(Call<Hospital> call, Response<Hospital> response) {
                Hospital hospital = response.body();
                Log.d("Result ", hospital.toString());
                onFinishedListener.onFinished(hospital);
            }

            @Override
            public void onFailure(Call<Hospital> call, Throwable t) {
                Log.d("Result Error ", t.toString());
                onFinishedListener.onFailure(t);
            }
        });
    }

}
