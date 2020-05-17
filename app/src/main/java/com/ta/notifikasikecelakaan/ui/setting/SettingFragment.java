package com.ta.notifikasikecelakaan.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.ui.login.LoginActivity;
import com.ta.notifikasikecelakaan.ui.setting.editpassword.EditPasswordActivity;
import com.ta.notifikasikecelakaan.ui.setting.editprofile.EditProfileActivity;
import com.ta.notifikasikecelakaan.utils.Constans;

public class SettingFragment extends Fragment implements View.OnClickListener{

    private SharedPreferences sharedPreferences;

    private TextView tvEditProfile;
    private TextView tvEditPassword;

    private Button btnOut;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_setting, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Constans.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);

        tvEditProfile = (TextView) root.findViewById(R.id.tv_edit_profile);
        tvEditProfile.setOnClickListener(this);
        tvEditPassword = (TextView) root.findViewById(R.id.tv_edit_pass);
        tvEditPassword.setOnClickListener(this);

        btnOut = (Button) root.findViewById(R.id.btn_out);
        btnOut.setOnClickListener(this);


        return root;
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
                sharedPreferences.edit().remove(Constans.TAG_TOKEN).commit();
                sharedPreferences.edit().remove(Constans.TAG_ID_RESPONDENT).commit();
                sharedPreferences.edit().remove(Constans.TAG_SESSION).commit();
                Intent iLogin = new Intent(getContext(), LoginActivity.class);
                startActivity(iLogin);
                break;
        }
    }
}