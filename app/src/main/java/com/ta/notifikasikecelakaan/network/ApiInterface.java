package com.ta.notifikasikecelakaan.network;


import com.ta.notifikasikecelakaan.model.Accident;
import com.ta.notifikasikecelakaan.model.Gallery;
import com.ta.notifikasikecelakaan.model.History;
import com.ta.notifikasikecelakaan.model.Hospital;
import com.ta.notifikasikecelakaan.model.PoliceOffice;
import com.ta.notifikasikecelakaan.model.Respondent;
import com.ta.notifikasikecelakaan.model.RespondentUpdate;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface ApiInterface {

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> requestLogin(@Field("phone") String phone,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseBody> requestRegister(@Field("name") String name,
                                       @Field("phone") String phone,
                                       @Field("fam_phone") String fam_phone,
                                       @Field("password") String password);

    @Multipart
    @POST("upload-image.php")
    Call<ResponseBody> upload(@Part MultipartBody.Part file, @Part("description") RequestBody description);

    @GET("police-office.php")
    Call<List<PoliceOffice>> getPoliceOffice();

    @GET("police-office.php")
    Call<PoliceOffice> getPoliceOfficeLocation(@Query("id") String policeOfficeId);

    @GET("hospitals.php")
    Call<List<Hospital>> getHospitals();

    @GET("hospitals.php")
    Call<Hospital> getHospitalLocation(@Query("id") String hospitalId);

    @GET("gallery.php")
    Call<List<Gallery>> getGallery();

    @GET("gallery.php")
    Call<Gallery> getGalleryDetail(@Query("id") String galleryId);

    @GET("histories.php")
    Call<List<History>> getHistories();

    @GET("histories.php")
    Call<History> getHistoryDetail(@Query("id") int historyId);

    @GET("respondent.php")
    Call<Respondent> getRespondentDetail(@Query("id") String respondentId);

    @GET("accident.php")
    Call<Accident> getAccidentNotification();

    @PUT("respondent.php")
    Call<RespondentUpdate> updateRespondent(@Body RespondentUpdate respondentUpdate,
                                            @Query("id") String idRespondent);
}
