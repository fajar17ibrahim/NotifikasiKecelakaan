package com.ta.notifikasikecelakaan.ui.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.network.ApiInterface;
import com.ta.notifikasikecelakaan.ui.login.LoginActivity;
import com.ta.notifikasikecelakaan.utils.ApiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrasiActivity extends AppCompatActivity implements View.OnClickListener {

    private String token = "null";
    private ProgressDialog loading;

    private Context mContext;
    private ApiInterface mApiService;

    private CameraPosition Current;

    private EditText eNama;
    private EditText eTelp;
    private EditText ePass;
    private EditText eConfPass;
    private String nama;
    private String telp;
    private String pass;
    private String confPass;

    private Double latitude;
    private Double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        currentLocation();

        mContext = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ganti icon nav drawer
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnDaftar = (Button) findViewById(R.id.btn_daftar);
        eNama = (EditText) findViewById(R.id.nama);
        eTelp = (EditText) findViewById(R.id.telepon);
        ePass = (EditText) findViewById(R.id.pass);
        eConfPass = (EditText) findViewById(R.id.kon_pass);
        btnDaftar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_daftar:

                nama = eNama.getText().toString();
                telp = eTelp.getText().toString();
                pass = ePass.getText().toString();
                confPass = eConfPass.getText().toString();

                if (nama.trim().length() == 0 || telp.trim().length() == 0 || pass.trim().length() == 0 || confPass.trim().length() == 0) {
                    Toast.makeText(mContext, "Field tidak boleh kosong! ", Toast.LENGTH_SHORT).show();
                } else if (pass.equals(confPass)){
                    loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                    requestRegister(nama, telp, pass);
                } else {
                    Toast.makeText(mContext, "Password tidak cocok! ", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void requestRegister(final String name, final String phone, final String password) {
        Log.d("Result ", phone + " " + password);
        mApiService = ApiUtils.getAPIService();
        mApiService.requestRegister(name, phone, password, latitude, longitude, token)
                .enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("success").equals("1")) {
                                    // Jika register berhasil maka data nama yang ada di response API
                                    String message = jsonRESULTS.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(mContext, LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    // Jika register gagal
                                    String error_message = jsonRESULTS.getString("message");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, "Registrasi Gagal! Coba lagi", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
    }

    public void currentLocation() {

//      GET CURRENT LOCATION
        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    // Do it all with location
                    Log.d("My Current location", "Lat : " + location.getLatitude() + " Long : " + location.getLongitude());
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                }
            }
        });

    }

}
