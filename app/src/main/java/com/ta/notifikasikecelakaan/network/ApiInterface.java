package com.ta.notifikasikecelakaan.network;


import com.ta.notifikasikecelakaan.model.PoliceOffice;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> requestLogin(@Field("phone") String phone,
                                    @Field("password") String password);

    @GET("police-office.php")
    Call<List<PoliceOffice>> getPoliceOffice();


}
