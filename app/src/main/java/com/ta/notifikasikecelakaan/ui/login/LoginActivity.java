package com.ta.notifikasikecelakaan.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;

//        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        {
//            if (conMgr.getActiveNetworkInfo() != null
//                    && conMgr.getActiveNetworkInfo().isAvailable()
//                    && conMgr.getActiveNetworkInfo().isConnected()) {
//            } else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection",
//                        Toast.LENGTH_LONG).show();
//            }
//        }

        Button btnMasuk = findViewById(R.id.btn_masuk);
        btnMasuk.setOnClickListener(this);
        tvRegistrasi = findViewById(R.id.registrasi);
        tvRegistrasi.setOnClickListener(this);
        txt_telp = (EditText) findViewById(R.id.txt_telp);
        txt_password = (EditText) findViewById(R.id.txt_pass);

        // Cek session login jika TRUE maka langsung buka MainActivity
//        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
//        session = sharedpreferences.getBoolean(session_status, false);
//
//        cek = sharedpreferences.getInt(TAG_CEK, 0);
//        id = sharedpreferences.getString(TAG_ID, null);
//        telp = sharedpreferences.getString(TAG_TELP, null);
//
//        if (session) {
//            Intent intent = new Intent(LoginActivity.this, KendaraanActivity.class);
//            intent.putExtra(TAG_ID, id);
//            intent.putExtra(TAG_TELP, telp);
//            intent.putExtra(TAG_CEK, cek);
//            finish();
//            startActivity(intent);
//        }
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
        mApiService.requestLogin(phone, password)
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
                                    String token = jsonRESULTS.getString("token");
                                    sharedPreferences = getSharedPreferences(Constans.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(Constans.TAG_ID_RESPONDENT, id);
                                    editor.putString(Constans.TAG_TOKEN, token);
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

}
