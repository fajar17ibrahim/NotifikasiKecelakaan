package com.ta.notifikasikecelakaan.ui.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.ui.setting.editpassword.EditPasswordActivity;
import com.ta.notifikasikecelakaan.ui.setting.editprofile.EditProfileActivity;

public class SettingFragment extends Fragment implements View.OnClickListener{

    private TextView tvEditProfile, tvEditPassword;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_setting, container, false);

        tvEditProfile = (TextView) root.findViewById(R.id.tv_edit_profile);
        tvEditProfile.setOnClickListener(this);
        tvEditPassword = (TextView) root.findViewById(R.id.tv_edit_pass);
        tvEditPassword.setOnClickListener(this);

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
        }
    }
}