package com.ta.notifikasikecelakaan.ui.setting.editpassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ta.notifikasikecelakaan.MainActivity;
import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.network.ApiClient;
import com.ta.notifikasikecelakaan.network.ApiInterface;
import com.ta.notifikasikecelakaan.ui.history.HistoriesContract;
import com.ta.notifikasikecelakaan.utils.ApiUtils;
import com.ta.notifikasikecelakaan.utils.Constans;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPasswordActivity extends AppCompatActivity  {

    private SharedPreferences sharedPreferences;

    private Button btnSave;
    private EditText eOldPass;
    private EditText eNewPass;
    private EditText eConfPass;

    private String sOldPass;
    private String sNewPass;
    private String sConfPass;
    private String respondentId;

    private Context mContext;
    private ProgressDialog loading;

    private ApiInterface mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        mContext = this;

        sharedPreferences = getSharedPreferences(Constans.MY_SHARED_PREFERENCES, MODE_PRIVATE);
        respondentId = sharedPreferences.getString(Constans.TAG_RESPONDENT_ID, "0");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        eOldPass = (EditText) findViewById(R.id.txt_old_pass);
        eNewPass = (EditText) findViewById(R.id.txt_new_pass);
        eConfPass = (EditText) findViewById(R.id.txt_conf_pass);

        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekOldPassword();
            }
        });
    }

    private void cekOldPassword() {
        sOldPass = eOldPass.getText().toString();
        sNewPass = eNewPass.getText().toString();
        sConfPass = eConfPass.getText().toString();

        if (sOldPass.trim().length() == 0 || sNewPass.trim().length() == 0 || sConfPass.trim().length() == 0 ) {
            Toast.makeText(mContext, "Field tidak boleh kosong! ", Toast.LENGTH_SHORT).show();
        } else if (sNewPass.equals(sConfPass)){
            loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
            requestUpdatePassword(sOldPass, sNewPass);
        } else {
            Toast.makeText(mContext, "Password tidak cocok! ", Toast.LENGTH_SHORT).show();
        }

    }

    private void requestUpdatePassword(String sOldPass, String sNewPass) {

        mApiService = ApiUtils.getAPIService();
        mApiService.updatePassword(sOldPass, sNewPass, respondentId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("success").equals("1")) {
                                    // Jika register berhasil maka data nama yang ada di response API
                                    String message = jsonRESULTS.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(mContext, MainActivity.class);
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
                            Toast.makeText(mContext, "Update Gagal! Coba lagi", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        Toast.makeText(mContext, t.toString(), Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
    }

}
