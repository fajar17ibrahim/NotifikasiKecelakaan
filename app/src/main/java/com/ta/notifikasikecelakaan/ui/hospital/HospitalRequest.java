package com.ta.notifikasikecelakaan.ui.hospital;

import android.util.Log;

import com.ta.notifikasikecelakaan.model.Hospital;
import com.ta.notifikasikecelakaan.network.ApiClient;
import com.ta.notifikasikecelakaan.network.ApiInterface;
import com.ta.notifikasikecelakaan.ui.policeoffice.PoliceOfficeContract;
import com.ta.notifikasikecelakaan.utils.ApiUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalRequest implements HospitalContract.Model {

    @Override
    public void getHospitals(final OnFinishedListener onFinishedListener, String history_id) {
        ApiInterface apiInterface = ApiClient.getClient(ApiUtils.BASE_URL_API).create(ApiInterface.class);

        Call<List<Hospital>> call = apiInterface.getHospitals(history_id);
        call.enqueue(new Callback<List<Hospital>>() {
            @Override
            public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {
                List hospitals = response.body();
                Log.d("Result ", hospitals.toString());
                onFinishedListener.onFinished(hospitals);
            }

            @Override
            public void onFailure(Call<List<Hospital>> call, Throwable t) {
                Log.d("Result Error ", t.toString());
                onFinishedListener.onFailure(t);
            }
        });
    }
}
