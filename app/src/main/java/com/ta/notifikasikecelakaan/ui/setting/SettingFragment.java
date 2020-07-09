package com.ta.notifikasikecelakaan.ui.setting;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.network.ApiInterface;
import com.ta.notifikasikecelakaan.ui.login.LoginActivity;
import com.ta.notifikasikecelakaan.ui.setting.editpassword.EditPasswordActivity;
import com.ta.notifikasikecelakaan.ui.setting.editprofile.EditProfileActivity;
import com.ta.notifikasikecelakaan.utils.ApiUtils;
import com.ta.notifikasikecelakaan.utils.Constans;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment implements View.OnClickListener{

    private SharedPreferences sharedPreferences;
    private ApiInterface mApiService;
    private ProgressDialog loading;

    private Context mContext;

    private TextView tvEditProfile;
    private TextView tvEditPassword;
    private String token;

    private Button btnOut;
    private String historyId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_setting, container, false);

        mContext = getContext();

        sharedPreferences = getActivity().getSharedPreferences(Constans.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Constans.TAG_TOKEN, null);
        historyId = sharedPreferences.getString(Constans.TAG_HISTORY_ID, "0");

        tvEditProfile = (TextView) root.findViewById(R.id.tv_edit_profile);
        tvEditProfile.setOnClickListener(this);
        tvEditPassword = (TextView) root.findViewById(R.id.tv_edit_pass);
        tvEditPassword.setOnClickListener(this);

        btnOut = (Button) root.findViewById(R.id.btn_out);
        btnOut.setOnClickListener(this);


        return root;
    }

    private void requestLogout(){
        mApiService = ApiUtils.getAPIService();
        mApiService.requestLogout(token)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.dismiss();
                        Log.d("Response body", response.toString());
                        sharedPreferences.edit().remove(Constans.TAG_RESPONDENT_ID).commit();
                        sharedPreferences.edit().remove(Constans.TAG_RESPONDENT_LAT).commit();
                        sharedPreferences.edit().remove(Constans.TAG_RESPONDENT_LONG).commit();
                        sharedPreferences.edit().remove(Constans.TAG_HISTORY_ID).commit();
                        sharedPreferences.edit().remove(Constans.TAG_USER_ID).commit();
                        sharedPreferences.edit().remove(Constans.TAG_USER_LAT).commit();
                        sharedPreferences.edit().remove(Constans.TAG_USER_LONG).commit();
                        sharedPreferences.edit().remove(Constans.TAG_SESSION).commit();
                        Intent iLogin = new Intent(getContext(), LoginActivity.class);
                        startActivity(iLogin);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(mContext, t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.tv_edit_profile:
                Intent iEditProfile = new Intent(getContext(), EditProfileActivity.class);
                startActivity(iEditProfile);
                break;

            case  R.id.tv_edit_pass:
                Intent iEditPass = new Intent(getContext(), EditPasswordActivity.class);
                startActivity(iEditPass);
                break;

            case R.id.btn_out:
                if (historyId != "0") {
                    new AlertDialog.Builder(getContext()).setTitle("Konfirmasi")
                            .setMessage("Anda sedang menerima notifikasi. Tekan tombol 'Ok, Terimakasih!' pada halaman notifikasi untuk melakukan logout")
                            .setPositiveButton("OK", null).show();
                } else {
                    new AlertDialog.Builder(getContext()).setTitle("Konfirmasi")
                            .setMessage("Anda yakin ingin logout ?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                                    requestLogout();
                                }
                            }).setNegativeButton("Tidak", null).show();
                }

                break;
        }
    }
}