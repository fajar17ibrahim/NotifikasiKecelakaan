package com.ta.notifikasikecelakaan.fcmmysql;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.ta.notifikasikecelakaan.model.Accident;
import com.ta.notifikasikecelakaan.model.Respondent;
import com.ta.notifikasikecelakaan.network.ApiClient;
import com.ta.notifikasikecelakaan.network.ApiInterface;
import com.ta.notifikasikecelakaan.utils.ApiUtils;
import com.ta.notifikasikecelakaan.utils.Constans;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        getDataNetwork(token);
        Log.d("Firebsase Instance ID", "onTokenRefresh : "  + token);
        SharedPreferences sharedPreferences = getSharedPreferences(Constans.MY_SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constans.TAG_TOKEN, token);
        editor.apply();
    }

    private void getDataNetwork(String token){
        getObservable(token).subscribeWith(getObserver());
    }

    public Observable<Respondent> getObservable(String token){
        return ApiClient.getClient(ApiUtils.BASE_URL_API).create(ApiInterface.class)
                .register(token)
                .subscribeOn(Schedulers.io()) //async
                .observeOn(AndroidSchedulers.mainThread());
    }

    //
    public DisposableObserver<Respondent> getObserver(){
        return new DisposableObserver<Respondent>() {

            @Override
            public void onNext(Respondent respondent) {
                Log.d("onNext", respondent.getName());
            }

            @Override
            public void onError(Throwable e) {
                Log.d("Error", e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("Complate :", "complate");
            }

        };
    }

}
