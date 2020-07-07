package com.ta.notifikasikecelakaan.ui.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.ta.notifikasikecelakaan.MainActivity;
import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.network.ApiInterface;
import com.ta.notifikasikecelakaan.ui.register.RegistrasiActivity;
import com.ta.notifikasikecelakaan.utils.ApiUtils;
import com.ta.notifikasikecelakaan.utils.Constans;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sharedPreferences;
    private ProgressDialog loading;

    private Context mContext;
    private ApiInterface mApiService;

    private TextView tvRegistrasi;
    private EditText txt_telp, txt_password;

    private Boolean session = false;
    private String token;

    private Dialog dialog;
    private LayoutInflater inflater;
    private View dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseInstanceId.getInstance().getToken();

        mContext = this;

        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedPreferences = getSharedPreferences(Constans.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        session = sharedPreferences.getBoolean(Constans.TAG_SESSION, false);
        token = sharedPreferences.getString(Constans.TAG_TOKEN, null);
        Log.d("Token dari Server ", token);

        if (session) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        Button btnMasuk = findViewById(R.id.btn_masuk);
        btnMasuk.setOnClickListener(this);
        tvRegistrasi = findViewById(R.id.registrasi);
        tvRegistrasi.setOnClickListener(this);
        txt_telp = (EditText) findViewById(R.id.txt_telp);
        txt_password = (EditText) findViewById(R.id.txt_pass);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registrasi:
                Intent intentDaftar = new Intent(this, RegistrasiActivity.class);
                startActivity(intentDaftar);
                break;
            case R.id.btn_masuk:
                // TODO Auto-generated method stub
                String phone = txt_telp.getText().toString();
                String password = txt_password.getText().toString();

                // mengecek kolom yang kosong
                if (phone.trim().length() > 0 && password.trim().length() > 0) {
                    loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                    requestLogin(phone, password);
                } else {
                    Toast.makeText(getApplicationContext() ,"Field tidak boleh kosong", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    private void requestLogin(String phone,  String password){
        Log.d("Result ", phone + " "+ password);
        mApiService = ApiUtils.getAPIService();
        mApiService.requestLogin(phone, password, token)
            .enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()){
                        loading.dismiss();
                        try {
                            JSONObject jsonRESULTS = new JSONObject(response.body().string());
                            if (jsonRESULTS.getString("success").equals("1")){
                                // Jika login berhasil maka data nama yang ada di response API
                                String name = jsonRESULTS.getString("name");
                                Toast.makeText(mContext, "Selamat Datang  " + name, Toast.LENGTH_SHORT).show();
                                String id = jsonRESULTS.getString("id");
                                sharedPreferences = getSharedPreferences(Constans.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Constans.TAG_RESPONDENT_ID, id);
                                editor.putBoolean(Constans.TAG_SESSION, true);
                                editor.apply();

                                Intent intent = new Intent(mContext, MainActivity.class);
                                startActivity(intent);
                            } else {
                                // Jika login gagal
                                String error_message = jsonRESULTS.getString("message");
                                Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(mContext, "Login Gagal! Coba lagi", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Keluar")
                .setMessage("Keluar aplikasi ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                    }
                }).setNegativeButton("Tidak", null).show();
    }

}
