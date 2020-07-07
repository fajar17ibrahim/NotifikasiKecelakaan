package com.ta.notifikasikecelakaan.ui.setting.editprofile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.model.Respondent;
import com.ta.notifikasikecelakaan.model.RespondentUpdate;
import com.ta.notifikasikecelakaan.network.ApiInterface;
import com.ta.notifikasikecelakaan.utils.ApiUtils;
import com.ta.notifikasikecelakaan.utils.Constans;

import org.json.JSONException;
import org.json.JSONObject;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity implements ProfileContract.View {

    private SharedPreferences sharedpreferences;
    private ProfilePresenter profilePresenter;
    private String idRespondent;

    private ApiInterface mApiService;
    private ProgressDialog loading;
    private Context mContext;

    private EditText eName, ePhone, eFamPhone;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mContext = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedpreferences = getSharedPreferences(Constans.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        idRespondent = sharedpreferences.getString(Constans.TAG_RESPONDENT_ID, "id");

        profilePresenter = new ProfilePresenter(this);
        profilePresenter.requestDataFromServer(idRespondent);

        eName = (EditText) findViewById(R.id.txt_name);
        ePhone = (EditText) findViewById(R.id.txt_phone);
        eFamPhone = (EditText) findViewById(R.id.txt_fam_phone);
        btnUpdate = (Button) findViewById(R.id.btn_update);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RespondentUpdate respondentUpdate = new RespondentUpdate();
                respondentUpdate.setName(eName.toString());
                respondentUpdate.setPhone(ePhone.toString());
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                requestRespondentUpdate(respondentUpdate, idRespondent);
            }
        });
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setDataToView(Respondent respondent) {
        eName.setText(respondent.getName());
        ePhone.setText(respondent.getPhone());
        eFamPhone.setText(respondent.getPhone());
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Log.d("Error ", throwable.toString());
        Toast.makeText(this, "Data gagal dimuat.", Toast.LENGTH_LONG).show();
    }

    private void requestRespondentUpdate(RespondentUpdate respondent, String idRespondent){
        mApiService = ApiUtils.getAPIService();
        mApiService.updateRespondent(respondent, idRespondent)
                .enqueue(new Callback<RespondentUpdate>() {
                    @Override
                    public void onResponse(Call<RespondentUpdate> call, Response<RespondentUpdate> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().toString());
                                if (jsonRESULTS.getString("success").equals("1")){
                                    // Jika update berhasil maka data nama yang ada di response API
                                    String message = jsonRESULTS.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    // Jika update gagal
                                    String error_message = jsonRESULTS.getString("message");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, "Update Gagal! Coba lagi", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<RespondentUpdate> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
    }
}
