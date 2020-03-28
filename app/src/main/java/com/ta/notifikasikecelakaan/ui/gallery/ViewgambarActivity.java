package com.ta.notifikasikecelakaan.ui.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ta.notifikasikecelakaan.R;

public class ViewgambarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewgambar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String nama = getIntent().getStringExtra("nama");
        String jam = getIntent().getStringExtra("jam");
        String gambar = getIntent().getStringExtra("gambar");

        ImageView imgGambar = (ImageView) findViewById(R.id.imageView);
        Glide.with(ViewgambarActivity.this)
                .load(gambar)
                .apply(new RequestOptions().override(300, 200))
                .into(imgGambar);
        TextView tvKeterangan = (TextView) findViewById(R.id.tv_keterangan);
        tvKeterangan.setText("Gambar ini diambil oleh "+ nama +" di lokasi kecelakaan jam "+ jam +" WIB.");
     }
}
