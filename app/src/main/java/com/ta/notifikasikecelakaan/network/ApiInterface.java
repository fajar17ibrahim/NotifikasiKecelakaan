package com.ta.notifikasikecelakaan.network;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.ta.notifikasikecelakaan.model.Accident;
import com.ta.notifikasikecelakaan.model.Gallery;
import com.ta.notifikasikecelakaan.model.History;
import com.ta.notifikasikecelakaan.model.Hospital;
import com.ta.notifikasikecelakaan.model.PoliceOffice;
import com.ta.notifikasikecelakaan.model.Respondent;
import com.ta.notifikasikecelakaan.model.RespondentUpdate;
import com.ta.notifikasikecelakaan.model.UploadImage;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface ApiInterface {

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> requestLogin(@Field("phone") String phone,
                                    @Field("password") String password,
                                    @Field("token") String token);

    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseBody> requestRegister(@Field("name") String name,
                                       @Field("phone") String phone,
                                       @Field("password") String password,
                                       @Field("latitude") Double latitude,
                                       @Field("longitude") Double longitude,
                                       @Field("token") String token);

    @FormUrlEncoded
    @POST("logout.php")
    Call<ResponseBody> requestLogout(@Field("token") String token);

    @FormUrlEncoded
    @POST("victim_helped.php")
    Call<ResponseBody> requestUpdateStatus(@Field("history_id") String history_id,
                                           @Field("status") String status,
                                           @Field("respondent_id") String respondent_id,
                                           @Field("hospital_id") String hospital_id);

    @FormUrlEncoded
    @POST("update_normal.php")
    Call<ResponseBody> requestUpdateStatusNormal(@Field("history_id") String historyId);

    @FormUrlEncoded
    @POST("update_profile.php")
    Call<ResponseBody> updateRespondent(@Field("name") String name,
                                        @Field("phone") String phone,
                                        @Query("id") String idRespondent);

    @FormUrlEncoded
    @POST("update_password.php")
    Call<ResponseBody> updatePassword(@Field("old_password") String old_password,
                                      @Field("new_password") String new_password,
                                      @Query("id") String idRespondent);

    @Multipart
    @POST("upload_image.php")
    Call<UploadImage> uploadImage(@Part MultipartBody.Part image,
                                  @Part("history_id") RequestBody history_id,
                                  @Part("respondent_id") RequestBody respondent_id);

    @GET("police_office.php")
    Call<List<PoliceOffice>> getPoliceOffice(@Query("history_id") String history_id);

    @GET("hospitals.php")
    Call<List<Hospital>> getHospitals(@Query("history_id") String history_id);

    @GET("gallery.php")
    Call<List<Gallery>> getGallery(@Query("id") String historyId);

    @GET("gallery_details.php")
    Call<Gallery> getGalleryDetail(@Query("gallery_id") String galleryId);

    @GET("histories.php")
    Call<List<History>> getHistories(@Query("id") int respondent_id);

    @GET("histories.php")
    Call<History> getHistoryDetail(@Query("id") int historyId);

    @GET("respondent.php")
    Call<Respondent> getRespondentDetail(@Query("id") String respondentId);

    @FormUrlEncoded
    @POST("register_token.php")
    Observable<Respondent> register(@Field("token") String token);

    @GET("accident.php")
    Observable<Accident> getAccidentNotification(@Field("token") String token);

    @GET("accident.php")
    Call<Accident> getAccident(@Query("id") int user_id);



}
