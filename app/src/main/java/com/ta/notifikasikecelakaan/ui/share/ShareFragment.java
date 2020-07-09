package com.ta.notifikasikecelakaan.ui.share;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ta.notifikasikecelakaan.R;

public class ShareFragment extends Fragment {

    private Button btnShare;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_share, container, false);

        btnShare = (Button) root.findViewById(R.id.btn_share);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Install aplikasi Notifikasi Kecelakaan untuk mendapatkan notifikasi kecelakaan yang terjadi disekitar anda.\n\nDownload sekarang :\nhttps://drive.google.com/drive/folders/1-Y7fqc8BOyLw6x43xmW7Z6h7aydXvzUW?usp=sharing");
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(sendIntent, ""));
                startActivity(sendIntent);
            }
        });

        return root;
    }
}